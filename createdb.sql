CREATE TABLE MOVIE(
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
PRIMARY KEY(movieID,actorID),
FOREIGN KEY(movieID) REFERENCES MOVIE(id)
);

CREATE TABLE Movie_countries(
movieID	Integer,
country Varchar2(50),
PRIMARY KEY(movieID),
FOREIGN KEY(movieID) REFERENCES MOVIE(id)
);


CREATE TABLE Movie_directors(
movieID Integer,
directorID varchar2(256),
directorName varchar2(256),
PRIMARY KEY(movieID,directorID),
FOREIGN KEY(movieID) REFERENCES MOVIE(id)
);

CREATE TABLE Movie_genres(
movieID	Integer,
genre Varchar2(50),
PRIMARY KEY(movieID,genre),
FOREIGN KEY(movieID) REFERENCES MOVIE(id)
);
