# --- !Ups
ALTER TABLE task ADD COLUMN category bigint DEFAULT NULL;
ALTER TABLE task ADD CONSTRAINT category_fk FOREIGN KEY (category) REFERENCES category(id);
# --- !Downs
ALTER TABLE task DROP COLUMN category;