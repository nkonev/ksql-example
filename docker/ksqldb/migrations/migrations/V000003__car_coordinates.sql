CREATE OR REPLACE STREAM IF NOT EXISTS coordinates_stream(
    carid VARCHAR KEY,
    latitude DECIMAL(17,15),
    longitude DECIMAL(17,15)
) WITH (
    KAFKA_TOPIC = 'coordinates',
    KEY_FORMAT='KAFKA',
    VALUE_FORMAT = 'JSON',
    PARTITIONS=1,
    REPLICAS=1
);