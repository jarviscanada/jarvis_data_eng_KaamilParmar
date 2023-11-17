# Introduction
This project has numerous SQL tables and corresponding queries all in a queries.sql file. These tables are established 
within a PostgreSQL instance, which is deployed through a Docker container. The execution of queries can take place 
externally to the database by utilizing queries.sql. In my case, the queries were written and executed on dbeaver.
The objective is to demonstrate the structure of SQL tables and methodologies of making and executing queries in PostgreSQL,
The queries are all exercises from pgexercise, and it is assumed the given schema is set up in a database prior to 
executing and writing the queries (Setup provided here in the README.md). The query exercises showcase various important
SQL techniques from CRUD, Joins, Aggregate Functions, Strings, and other advanced queries.
# SQL Queries
###### Table Setup (DDL)
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
###### Q1 Insert calculated data into a table
```sql
INSERT INTO cd.facilities
( facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES (9, 'Spa', 20, 30, 100000, 800);
```
###### Q2 Insert calculated data into a table
```sql
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES
    ((SELECT max(facid) FROM cd.facilities) + 1, 'Spa', 20, 30, 100000, 800);
```

###### Q3  Update some existing data
```sql
UPDATE cd.facilities SET initialoutlay=10000 WHERE facid=1;
```

###### Q4  Update a row based on the contents of another row
```sql
UPDATE cd.facilities
SET membercost = (SELECT membercost FROM cd.facilities WHERE facid=0) * 1.1,
    guestcost = (SELECT guestcost FROM cd.facilities WHERE facid=0) * 1.1
WHERE facid=1;
```
###### Q5  Delete all bookings
```sql
DELETE FROM cd.bookings;
```

###### Q6  Delete a member from the cd.members table
```sql
DELETE FROM cd.members WHERE memid=37;
```
###### Basic
###### Q7  Control which rows are retrieved - part 2
```sql
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost < monthlymaintenance * 0.02 AND membercost > 0;
```
###### Q8  Basic string searches
```sql
SELECT * FROM cd.facilities WHERE name LIKE '%Tennis%';
```

###### Q9  Matching against multiple possible values
```sql
SELECT * FROM cd.facilities WHERE facid IN (1,5);
```

###### Q10 Working with dates
```sql
SELECT memid, surname, firstname, joindate FROM cd.members
WHERE joindate > '2012-08-31'::date;
```
###### Q11 Combining results from multiple queries
```sql
SELECT surname FROM cd.members UNION SELECT name FROM cd.facilities;
```
###### Join
###### Q12  Retrieve the start times of members' bookings
```sql
SELECT starttime FROM cd.bookings INNER JOIN cd.members
ON cd.bookings.memid = cd.members.memid
WHERE cd.members.surname = 'Farrell' AND cd.members.firstname = 'David';
```
###### Q13  Work out the start times of bookings for tennis courts
```sql
SELECT starttime, name
FROM cd.bookings
         INNER JOIN cd.facilities ON cd.bookings.facid = cd.facilities.facid
WHERE name LIKE '%Tennis Court%' AND starttime::date = '2012-09-21'::date
ORDER BY starttime;
```
###### Q14  Produce a list of all members, along with their recommender
```sql
SELECT
    members.firstname AS memfname,
    members.surname AS memsname,
    recommenders.firstname AS recfname,
    recommenders.surname AS recsname
FROM
    cd.members members
        LEFT OUTER JOIN cd.members recommenders ON recommenders.memid = members.recommendedby
ORDER BY
    memsname, memfname;
```
###### Q15  Produce a list of all members who have recommended another member
```sql
SELECT DISTINCT
    recommenders.firstname AS recfname,
    recommenders.surname AS recsname
FROM
    cd.members members
        INNER JOIN cd.members recommenders ON recommenders.memid = members.recommendedby
ORDER BY
    recsname, recfname;
```

###### Q16 Produce a list of all members, along with their recommender, using no joins.
```sql
SELECT DISTINCT
    CONCAT(members.firstname, ' ', members.surname) AS member,
    (SELECT DISTINCT CONCAT(recommenders.firstname, ' ', recommenders.surname)
     FROM cd.members recommenders WHERE recommenders.memid=members.recommendedby)
                                                    AS recommender
FROM
    cd.members members
ORDER BY
    member;
```

###### Q17  Count the number of recommendations each member makes.
```sql
SELECT recommendedby, COUNT(*) FROM cd.members WHERE cd.members.recommendedby IS NOT NULL
GROUP BY recommendedby ORDER BY recommendedby;
```

###### Q9  Matching against multiple possible values
```sql
SELECT * FROM cd.facilities WHERE facid IN (1,5);
```

