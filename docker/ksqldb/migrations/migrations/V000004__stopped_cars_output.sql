CREATE OR REPLACE TABLE  stopped_cars_output_table WITH (
              KAFKA_TOPIC = 'stopped_cars',
              KEY_FORMAT='KAFKA',
              VALUE_FORMAT = 'JSON',
              PARTITIONS=1,
              REPLICAS=1
)
-- we cannot consume carid key because it contains trash symbols so we put key into value with AS_VALUE function
AS SELECT carid, AS_VALUE(carid) as car, LATEST_BY_OFFSET(cast (latitude as double)) as latitude, LATEST_BY_OFFSET(cast(longitude as double)) as longitude
FROM coordinates_stream
            WINDOW TUMBLING (SIZE 90 SECONDS, GRACE PERIOD 90 DAYS)
GROUP BY carid
HAVING
              STDDEV_SAMP(CAST (latitude * 100000000000 AS bigint)) < 10 AND
              STDDEV_SAMP(CAST (longitude * 100000000000 AS bigint)) < 10

              EMIT CHANGES
;