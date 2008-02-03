USE banenmarktonline;

DROP TABLE IF EXISTS messages;

CREATE TABLE messages (
	id VARCHAR(100) NOT NULL PRIMARY KEY,
	item text NOT NULL,
	index(id)
) ENGINE=MYISAM DEFAULT CHARSET=LATIN1;

insert into messages(id, item) values('test1', 'message1');
insert into messages(id, item) values('test2', 'message {0} en nog {1}');


select * from messages;
