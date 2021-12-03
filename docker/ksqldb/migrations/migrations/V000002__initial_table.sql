CREATE TABLE users(
    userid BIGINT PRIMARY KEY,
    registertime BIGINT,
    gender VARCHAR,
    regionid VARCHAR
) WITH (
    KAFKA_TOPIC = 'users',
    VALUE_FORMAT='JSON',
    PARTITIONS=4,
    REPLICAS=1
);