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

--Aggregation
--Q17  Count the number of recommendations each member makes.
SELECT recommendedby, COUNT(*) FROM cd.members WHERE cd.members.recommendedby IS NOT NULL
GROUP BY recommendedby ORDER BY recommendedby;

--Q18  List the total slots booked per facility
SELECT facid, SUM(slots) as TotalSlots FROM cd.bookings GROUP BY cd.bookings.facid ORDER BY cd.bookings.facid;

--Q19  List the total slots booked per facility in a given month
SELECT facid, SUM(slots) as TotalSlots FROM cd.bookings
WHERE starttime::date>='2012-09-01'::date AND starttime::date<='2012-10-30'::date
GROUP BY cd.bookings.facid ORDER BY SUM(slots);

--Q20  List the total slots booked per facility per month
SELECT facid, EXTRACT(MONTH FROM starttime) AS month, SUM(slots)
FROM cd.bookings
WHERE starttime::date>='2012-01-01' AND starttime::date<='2012-12-31'
GROUP BY facid, month
ORDER BY facid, month;

--Q21  Find the count of members who have made at least one booking
SELECT COUNT(DISTINCT memid) as count FROM cd.bookings;

--Q22  List each member's first booking after September 1st 2012
SELECT surname, firstname, books.memid, MIN(starttime)
FROM cd.members mems JOIN cd.bookings books ON mems.memid=books.memid
WHERE starttime::date>='2012-09-01'::date
GROUP BY mems.surname, mems.firstname, books.memid
ORDER BY books.memid;

--Q23 Produce a list of member names, with each row containing the total member count
SELECT COUNT(*) over(), firstname, surname
FROM cd.members
ORDER BY joindate;

--Q24  Produce a numbered list of members
SELECT row_number() over(), firstname, surname FROM cd.members;

--Q25 Output the facility id that has the highest number of slots booked, again (soln from pgexercises site)
select facid, total from (
                             select facid, sum(slots) total, rank() over (order by sum(slots) desc) rank
                             from cd.bookings
                             group by facid
                         ) as ranked
where rank = 1;

--String
--Q26 Format the names of members
SELECT CONCAT(surname, ', ',firstname) as name FROM cd.members;

--Q27  Find telephone numbers with parentheses
SELECT memid, telephone FROM cd.members WHERE telephone LIKE '%(%)%';

--Q28 Count the number of members whose surname starts with each letter of the alphabet
SELECT SUBSTR(surname, 1, 1) as letter, COUNT(*)d
FROM cd.members
GROUP BY letter
ORDER BY letter;
