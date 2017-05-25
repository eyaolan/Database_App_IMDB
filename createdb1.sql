--this file just for demo
--removed every tables's Foreign key constraint
--for Populate.java to go through the process of populating 12 tables with limited data in short time.

CREATE TABLE MOVIES(
id	INTEGER ,
title	varchar2(256),
imdbID	INTEGER,
spanishTitle varchar2(256),
imdbPictureURL	varchar2(256),
year	INTEGER,
rtID	varchar2(256),
rtAllCriticsRating	number,
rtAllCriticsNumReviews	INTEGER,
rtAllCriticsNumFresh	INTEGER,
rtAllCriticsNumRotten	INTEGER,
rtAllCriticsScore	  INTEGER,
rtTopCriticsRating	  number,
rtTopCriticsNumReviews	INTEGER,
rtTopCriticsNumFresh	INTEGER,
rtTopCriticsNumRotten	INTEGER,
rtTopCriticsScore	    INTEGER,
rtAudienceRating	   number,
rtAudienceNumRatings	INTEGER,
rtAudienceScore	       INTEGER,
rtPictureURL  varchar2(256),
PRIMARY KEY (id)
);

CREATE TABLE Movie_actors(
movieID	Integer,
actorID	VARCHAR2(256),
actorName varchar2(256),
ranking Integer,
PRIMARY KEY(movieID,actorID)
);

CREATE TABLE Movie_countries(
movieID	Integer,
country Varchar2(50),
PRIMARY KEY(movieID)
);


CREATE TABLE Movie_directors(
movieID Integer,
directorID varchar2(256),
directorName varchar2(256),
PRIMARY KEY(movieID,directorID)
);

CREATE TABLE Movie_genres(
movieID	Integer,
genre Varchar2(50),
PRIMARY KEY(movieID,genre)
);

CREATE TABLE Movie_locations(
MovieID Integer,
location1 varchar2(256),
location2 varchar2(256),
location3 varchar2(256),
location4 varchar2(256)
);



CREATE TABLE tags(
id Integer,
value varchar2(256),
PRIMARY KEY(id)
);


CREATE TABLE Movie_tags(
movieID	Integer,
tagID	Integer,
tagWeight Integer,
PRIMARY KEY(movieID,tagID)
);

CREATE TABLE user_ratedmovies_timestamps(
userID	Integer,
movieID	Integer,
rating number,
timestamp Integer,
PRIMARY KEY(userID,movieID)
);

CREATE TABLE user_ratedmovies(
userID	Integer,
movieID	Integer,
rating	number,
date_day	Integer,
date_month	Integer,
date_year	Integer,
date_hour	Integer,
date_minute	Integer,
date_second Integer,
PRIMARY KEY(userID,movieID)
);

CREATE TABLE user_taggedmovies_timestamps(
userID	Integer,
movieID	Integer,
tagID	Integer,
timestamp Integer,
PRIMARY KEY(userID,movieID,tagID)
);

CREATE TABLE user_taggedmovies(
userID	Integer,
movieID	Integer,
tagID	Integer,
date_day	Integer,
date_month	Integer,
date_year	Integer,
date_hour	Integer,
date_minute	Integer,
date_second Integer,
PRIMARY KEY(userID,movieID,tagID)
);
