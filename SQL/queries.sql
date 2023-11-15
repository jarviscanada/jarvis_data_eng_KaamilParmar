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
UPDATE cd.facilities SET initialoutlay=10000 WHERE facid=1

--Q4   Update a row based on the contents of another row
UPDATE cd.facilities
SET membercost = (SELECT membercost FROM cd.facilities WHERE facid=0) * 1.1,
    guestcost = (SELECT guestcost FROM cd.facilities WHERE facid=0) * 1.1
WHERE facid=1


