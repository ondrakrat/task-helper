# --- !Ups
CREATE TABLE category (
     id    bigserial PRIMARY KEY,
     name   varchar NOT NULL
);

# --- !Downs
DROP TABLE category;