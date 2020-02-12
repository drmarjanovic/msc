CREATE TABLE updates (
  id         BIGSERIAL    NOT NULL PRIMARY KEY,
  user_id    BIGINT       NOT NULL,
  message    VARCHAR(160) NOT NULL,
  type       VARCHAR(20)  NOT NULL,
  created_at TIMESTAMP    NOT NULL DEFAULT now()
);
