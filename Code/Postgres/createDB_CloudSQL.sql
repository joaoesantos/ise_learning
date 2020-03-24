------------------------------------
--    CREATE DB FOR CLOUD SQL     --
------------------------------------

CREATE DATABASE db_name
	WITH
	OWNER =  owner_name		
	ENCODING = 'UTF8'		
	CONNECTION LIMIT = -1;

CREATE TABLE USERS (
	userID INT GENERATED ALWAYS AS IDENTITY,
	username VARCHAR(20) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	name VARCHAR(50) UNIQUE NOT NULL,
	email VARCHAR(20) UNIQUE NOT NULL,
	image BYTEA,
	--
	PRIMARY KEY (userID)
);

(...)