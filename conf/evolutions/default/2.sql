# --- !Ups
CREATE TABLE task (
     id    bigserial PRIMARY KEY,
     text   varchar NOT NULL,
     finished bool DEFAULT FALSE
);

# --- !Downs
DROP TABLE task;