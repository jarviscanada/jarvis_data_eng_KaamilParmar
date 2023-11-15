--This assumes all necessary tables and schemas have already been created

--Q1  Insert calculated data into a table
INSERT INTO cd.facilities
( facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES (9, 'Spa', 20, 30, 100000, 800);

--Q2  Insert calculated data into a table
INSERT INTO cd.facilities
(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES
    ((SELECT max(facid) FROM cd.facilities) + 1, 'Spa', 20, 30, 100000, 800);

--Q3  Update some existing data
UPDATE cd.facilities SET initialoutlay=10000 WHERE facid=1;

--Q4   Update a row based on the contents of another row
UPDATE cd.facilities
SET membercost = (SELECT membercost FROM cd.facilities WHERE facid=0) * 1.1,
    guestcost = (SELECT guestcost FROM cd.facilities WHERE facid=0) * 1.1
WHERE facid=1;

--Q5  Delete all bookings
DELETE FROM cd.bookings;

--Q6  Delete a member from the cd.members table
DELETE FROM cd.members WHERE memid=37;

--Basic
--Q7  Control which rows are retrieved - part 2
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost < monthlymaintenance * 0.02 AND membercost > 0;

--Q8  Basic string searches
SELECT * FROM cd.facilities WHERE name LIKE '%Tennis%';

--Q9  Matching against multiple possible values
SELECT * FROM cd.facilities WHERE facid IN (1,5);

--Q10  Working with dates
SELECT memid, surname, firstname, joindate FROM cd.members
WHERE joindate > '2012-08-31'::date;

--Q11  Combining results from multiple queries
SELECT surname FROM cd.members UNION SELECT name FROM cd.facilities;

--Join
--Q12  Retrieve the start times of members' bookings
SELECT starttime FROM cd.bookings INNER JOIN cd.members
ON cd.bookings.memid = cd.members.memid
WHERE cd.members.surname = 'Farrell' AND cd.members.firstname = 'David';

--Q13  Work out the start times of bookings for tennis courts
SELECT starttime, name
FROM cd.bookings
         INNER JOIN cd.facilities ON cd.bookings.facid = cd.facilities.facid
WHERE name LIKE '%Tennis Court%' AND starttime::date = '2012-09-21'::date
ORDER BY starttime;

--Q14  Produce a list of all members, along with their recommender
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

--Q15  Produce a list of all members who have recommended another member
SELECT DISTINCT
    recommenders.firstname AS recfname,
    recommenders.surname AS recsname
FROM
    cd.members members
        INNER JOIN cd.members recommenders ON recommenders.memid = members.recommendedby
ORDER BY
    recsname, recfname;

--Q16 Produce a list of all members, along with their recommender, using no joins.
SELECT DISTINCT
    CONCAT(members.firstname, ' ', members.surname) AS member,
    (SELECT DISTINCT CONCAT(recommenders.firstname, ' ', recommenders.surname)
     FROM cd.members recommenders WHERE recommenders.memid=members.recommendedby)
                                                    AS recommender
FROM
    cd.members members
ORDER BY
    member;