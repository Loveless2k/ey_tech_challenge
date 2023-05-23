create table users (
  id UUID,
  name varchar(255) NOT NULL,
  email VARCHAR(255) UNIQUE,
  gender VARCHAR(20),
  password VARCHAR(300) NOT NULL,
  created TIMESTAMP,
  modified TIMESTAMP,
  lastLogin TIMESTAMP,
  token VARCHAR(255),
  primary key (id)
);