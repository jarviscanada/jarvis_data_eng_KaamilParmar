# Linux Cluster Modeling
## Introduction
This project involved the development of a monitoring system to record hardware specifications and usage, 
storing the data in a database. The project's users are individuals managing machine clusters for diverse 
projects, aiding them in deciding whether to scale vertically or horizontally based on their requirements. 
The implementation leveraged Bash, Docker, Postgres, and Git. Testing was conducted manually on a CentOS virtual 
machine within the Google Cloud Platform. Additionally, crontab was employed to schedule regular data collection tasks.

## Quick Start
```bash
# Start a psql instance using psql_docker.sh
./scripts/psql_docker.sh create [username] [password]
# or if instance already exists
./scripts/psql_docker.sh start

# Create tables using ddl.sql
export PGPASSWORD=[password]
psql -h localhost -U [username] -c "CREATE DATABASE host_agent;" #(skip if already created)
psql -h localhost -U [username] -d host_agent -f ./sql/ddl.sql

# Insert hardware specs data into the DB using host_info.sh
./scripts/host_info.sh localhost 5432 host_agent [username] [password]

# Insert hardware usage data into the DB using host_usage.sh
./scripts/host_usage.sh localhost 5432 host_agent [username] [password]

# Crontab setup (inside square brackets is what to input in the file)
crontab -e [* * * * * bash /global/path/to/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log]
#To stop cron job
crontab - e []
```

## Implementation
To set up a PostgreSQL instance, a Docker container is created and initiated. The program is built using Linux commands
The ddl.sql file is used to create tables. Then, scripts like host_info.sh and host_usage.sh for data insertion into the 
table and parse hardware information.  Finally, crontab is configured to monitor usage at regular intervals by calling 
the host_usage.sh every minute and populating the database with information.

## Architecture


## Scripts
### psql_docker.sh
Initiates, halts, or launches a Docker container based on the provided arguments.
```bash 
#to create
./scripts/psql_docker.sh create [username] [password]

#to start (if already created)
./scripts/psql_docker.sh start

#to stop
./scripts/psql_docker.sh stop
```
### host_info.sh
Gathers hardware details from a Linux instance, executed during installation, and inserts this data into the database.
```bash 
#To run, username and password are what you use for your psql database
./scripts/host_info.sh localhost 5432 host_agent [username] [password]
```

### host_usage.sh
Captures hardware usage data from a Linux node and integrates it into the database. Is to be run every minute through the use of a cron job.
```bash 
#To run, username and password are what you use for your psql database
./scripts/host_info.sh localhost 5432 host_agent [username] [password]
```
### crontab
Automates the process of collecting data every minute by executing host_usage.sh
```bash 
# Crontab setup (inside square brackets is what to input in the file)
crontab -e [* * * * * bash /global/path/to/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log]
#To stop cron job
crontab - e []
```

## Database Modeling
The design for the host_info and host_usage schemas are as follows.
### Host Info
| Column Name      | Data Type | Constraints      |
|------------------|-----------|------------------|
| timestamp        | TIMESTAMP | NOT NULL         |
| host_id          | SERIAL    | NOT NULL         |
| memory_free      | INT4      | NOT NULL         |
| cpu_idle         | INT2      | NOT NULL         |
| cpu_kernel       | INT2      | NOT NULL         |
| disk_io          | INT4      | NOT NULL         |
| disk_available   | INT4      | NOT NULL         |

### Host Usage
| Column Name       | Data Type   | Constraints           |
|-------------------|-------------|-----------------------|
| id                | SERIAL      | NOT NULL, PRIMARY KEY |
| hostname          | VARCHAR     | NOT NULL, UNIQUE      |
| cpu_number        | INT2        | NOT NULL              |
| cpu_architecture  | VARCHAR     | NOT NULL              |
| cpu_model         | VARCHAR     | NOT NULL              |
| cpu_mhz           | FLOAT8      | NOT NULL              |
| l2_cache          | INT4        | NOT NULL              |
| timestamp         | TIMESTAMP   | NULL                  |
| total_mem         | INT4        | NULL                  |


## Test
For testing the bash scripts, all testing was done in the terminal using the `-xv` flag during executing, which helped debug line-by-line
what and where the bug could be occurring.

For testing ddl.sql, sample inserts were done first prior to executing the bash scripts to ensure the schema and method of inserting was all working.
then you would log into your psql docker instance and:
` \dt` on the terminal to check all databases and see if it was created, then call a select all statement, `SELECT * FROM [database]`, in the terminal to see if the inserts worked.

## Deployment
Deployment was done using Git, Github, Docker, and crontab.
+ Git and Github to deploy the codes
+ Docker to deploy the PSQL database
+ Crontab to execute the bash script

## Improvements
+ Write more nuanced comments in bash scripts
+ Become more competent at VIM editor for assigning cron jobs
+ Set a script to check whenever info from `host_info.sh` updates





