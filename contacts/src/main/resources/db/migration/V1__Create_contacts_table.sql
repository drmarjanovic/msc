CREATE TABLE contacts (
  id         BIGSERIAL   NOT NULL PRIMARY KEY,
  user_id    BIGINT      NOT NULL,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL,
  email      VARCHAR(50),
  mobile     VARCHAR(20),
  vip        BOOLEAN     NOT NULL DEFAULT FALSE
);
