# Introduction
This project has numerous SQL tables and corresponding queries all in a queries.sql file. These tables are established 
within a PostgreSQL instance, which is deployed through a Docker container. The execution of queries can take place 
externally to the database by utilizing queries.sql. In my case, the queries were written and executed on dbeaver.
The objective is to demonstrate the structure of SQL tables and methodologies of making and executing queries in PostgreSQL,
The queries are all exercises from pgexercise, and it is assumed the given schema is set up in a database prior to 
executing and writing the queries (Setup provided here in the README.md). The query exercises showcase various important
SQL techniques from CRUD, Joins, Aggregate Functions, Strings, and other advanced queries.
# Table Setup
```sql
CREATE TABLE cd.members (
    memid INTEGER NOT NULL NOT NULL,
    surname character varying(200) NOT NULL,
    firstname character varying(200) NOT NULL,
    address character varying(300) NOT NULL,
    zipcode INTEGER NOT NULL,
    telephone character varying(20) NOT NULL,
    recommendedby INTEGER,
    joindate timestamp NOT NULL,
    CONSTRAINT members_pk PRIMARY KEY (memid),
    CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
        REFERENCES cd.members(memid) ON DELETE SET NULL
);

CREATE TABLE cd.facilities (
    facid INTEGER NOT NULL,
    name character varying(100),
    membercost numeric NOT NULL,
    guestcost numeric NOT NULL,
    initialoutlay numeric NOT NULL,
    monthlymaintenance numeric NOT NULL,
    CONSTRAINT facilities_pk PRIMARY KEY (facid)
);

CREATE TABLE cd.bookings (
    facid INTEGER NOT NULL,
    memid INTEGER NOT NULL,
    starttime timestamp NOT NULL,
    slots INTEGER NOT NULL,
    CONSTRAINT bookings_pk PRIMARY KEY (facid),
    CONSTRAINT fk_bookings_memid FOREIGN KEY (memid)
        REFERENCES cd.members(memid),
    CONSTRAINT fk_bookings_facid FOREIGN KEY (facid)
        REFERENCES cd.facilities(facid)
);



```
##~~~~
