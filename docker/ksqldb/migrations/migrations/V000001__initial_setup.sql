CREATE STREAM advices_original (
    identifier varchar,
    message varchar,
    datetime varchar
) WITH (
    kafka_topic='advice-topic',
    value_format='JSON',
    PARTITIONS=4,
    REPLICAS=1
);
