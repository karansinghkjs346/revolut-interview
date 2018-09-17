CREATE TABLE user (
    id SERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    balance DECIMAL DEFAULT 0 NOT NULL,
    version bigint NOT NULL,
    PRIMARY KEY(id)
);

CREATE SEQUENCE user_version_seq;

CREATE TRIGGER insert_user_version_trigger BEFORE INSERT ON user FOR EACH ROW CALL "com.crixal.interview.db.trigger.UpdateVersionTrigger";
CREATE TRIGGER update_user_version_trigger BEFORE UPDATE ON user FOR EACH ROW CALL "com.crixal.interview.db.trigger.UpdateVersionTrigger";