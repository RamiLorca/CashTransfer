BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transfer CASCADE;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	username varchar(50) NOT NULL,
	balance numeric(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id)
);


-- Sequence to start transfer_id values at 3001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_transfer_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;

CREATE TABLE transfer (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	transfer_status varchar(50) NOT NULL,
	sender_id int NOT NULL,
	receiver_id int NOT NULL,
	amount numeric(13, 2) NOT NULL,
	time_sent timestamp NOT NULL,
	CONSTRAINT PK_transfer PRIMARY KEY (transfer_id)
);

--Adding Foreign Key Constraints--

ALTER TABLE account ADD CONSTRAINT FK_account_tenmo_user FOREIGN KEY (username) REFERENCES tenmo_user (username);
ALTER TABLE transfer ADD CONSTRAINT FK_acount_sender FOREIGN KEY (sender_id) REFERENCES account (account_id);
ALTER TABLE transfer ADD CONSTRAINT FK_acount_receiver FOREIGN KEY (receiver_id) REFERENCES account (account_id);

--Adding Users--

INSERT INTO tenmo_user (username, password_hash)
	VALUES ('eric_cameron_1', '$2a$10$eO6WQbesPXmpCsWQ2qK9OOKK9dpZ5tQORLaX9afr26nIkMZm8dXPe');

INSERT INTO tenmo_user (username, password_hash)
	VALUES ('sean_oberc_2', '$2a$10$tjFqrGHBrjaihjbq9/IOf.o3XRj7ePan0fNOCAZBQ4c8Ibr1xFJsW');

INSERT INTO tenmo_user (username, password_hash)
	VALUES ('admin_1', '$2a$10$t4D2Htu.5/ogUiLTpKMLfOJvLqlcHfD46NxyQ3w05rN2ufjPmZKhu');

COMMIT;



