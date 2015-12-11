CREATE KEYSPACE complex
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 1
};

CREATE TYPE complex.address (
   street text,
   city text,
   zipCode int,
   phones list<text>
);

CREATE TABLE complex.accounts (
   email text PRIMARY KEY,
   name text,
   addr frozen<address>
);





CREATE KEYSPACE timeline
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 2
};


CREATE TABLE timeline.post (
	userid text,
	messageid timeuuid,
	PRIMARY KEY (userid, messageid)
) WITH CLUSTERING ORDER BY (messageid DESC);

CREATE TABLE timeline.message (
	messageid timeuuid,
	content text,
	PRIMARY KEY (messageid)
);

CREATE TABLE timeline.timeline (
	userid text,
	messageid timeuuid,
	ownerid text,
	PRIMARY KEY (userid, messageid)
) WITH CLUSTERING ORDER BY (messageid DESC);

CREATE TABLE timeline.follow (
	userid text,
	followid text,
	PRIMARY KEY (userid, followid)
);

CREATE INDEX follower ON timeline.follow (followid);
CREATE INDEX timelineowner ON timeline.timeline (ownerid);
