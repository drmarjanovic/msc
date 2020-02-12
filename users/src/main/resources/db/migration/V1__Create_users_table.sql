CREATE TABLE users (
  id         BIGSERIAL   NOT NULL PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL,
  email      VARCHAR(50) UNIQUE NOT NULL,
  password   VARCHAR(40) NOT NULL
);
