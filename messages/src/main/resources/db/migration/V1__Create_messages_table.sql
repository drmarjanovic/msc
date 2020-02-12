CREATE TABLE messages (
  id         BIGSERIAL    NOT NULL PRIMARY KEY,
  user_id    BIGINT       NOT NULL,
  contact_id BIGINT       NOT NULL,
  body       VARCHAR(160) NOT NULL,
  sent_at    TIMESTAMP    NOT NULL DEFAULT now()
);
