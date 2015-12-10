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