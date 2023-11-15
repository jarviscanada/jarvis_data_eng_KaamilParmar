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
    recommendedby INTEGER NOT NULL,
    joindate timestamp NOT NULL
);

```
##