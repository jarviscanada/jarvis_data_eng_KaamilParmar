# Introduction

# Table Setup
```postgresql
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

CREATE TABLE cd.bookings (
    facid INTEGER NOT NULL,
    memid INTEGER NOT NULL,
    starttime timestamp NOT NULL,
    slots INTEGER NOT NULL,
    CONSTRAINT bookings_pk PRIMARY KEY (facid),
    CONSTRAINT fk_bookings_memid FOREIGN KEY (memid)
        REFERENCES cd.bookings(facid)
);

```
##~~~~
