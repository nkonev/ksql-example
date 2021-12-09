# KSQL
* https://docs.confluent.io/current/ksql/docs/developer-guide/syntax-reference.html
* https://blog.knoldus.com/ksql-streams-and-tables/
* https://blog.knoldus.com/ksql-getting-started-with-streaming-sql-for-apache-kafka/
* https://www.confluent.io/blog/troubleshooting-ksql-part-1/#later-offset
* https://docs.ksqldb.io/en/latest/tutorials/event-driven-microservice/
* https://docs.ksqldb.io/en/latest/developer-guide/ksqldb-clients/java-client/#event-driven-microservice
* https://docs.ksqldb.io/en/latest/developer-guide/ksqldb-clients/java-client/
* https://docs.ksqldb.io/en/latest/developer-guide/ksqldb-reference/select-pull-query/
* https://docs.ksqldb.io/en/latest/operate-and-deploy/migrations-tool/
* https://docs.ksqldb.io/en/latest/concepts/queries/
* https://docs.ksqldb.io/en/v0.7.0-ksqldb/developer-guide/create-a-table/
* https://docs.ksqldb.io/en/latest/developer-guide/ksqldb-reference/create-table/
* https://docs.ksqldb.io/en/latest/how-to-guides/update-a-running-persistent-query/
* Primary key behaviour https://docs.ksqldb.io/en/latest/reference/sql/data-definition/
* Tombstone https://rmoff.net/2020/11/03/kafka-connect-ksqldb-and-kafka-tombstone-messages/
* Tombstone and delete https://stackoverflow.com/questions/66305527/how-to-delete-a-value-from-ksqldb-table-or-insert-a-tombstone-value/66314510#66314510
* https://ksqldb.io/examples.html
* https://ksqldb.io/quickstart.html
* https://docs.ksqldb.io/en/latest/developer-guide/ksqldb-reference/scalar-functions/#geo_distance
* https://docs.ksqldb.io/en/latest/developer-guide/ksqldb-reference/aggregate-functions/#latest_by_offset
* https://docs.ksqldb.io/en/latest/reference/sql/time/ Time-based operations, like windowing, process records according to the timestamp in ROWTIME. By default, the implicit ROWTIME pseudo column is the timestamp of a message in a Kafka topic. Timestamps have an accuracy of one millisecond.
* https://davidxiang.com/2021/01/10/kafka-as-a-database/

```
SELECT 
    carid, 
    GEO_DISTANCE(EARLIEST_BY_OFFSET(latitude), EARLIEST_BY_OFFSET(longitude), LATEST_BY_OFFSET(latitude), LATEST_BY_OFFSET(longitude), 'KM') as distance
FROM coordinates_stream 
WINDOW TUMBLING (SIZE 90 SECONDS, GRACE PERIOD 90 DAYS)
GROUP BY carid
EMIT CHANGES;

CREATE TABLE TRACKS_TABLE
AS SELECT 
    carid, 
    GEO_DISTANCE(EARLIEST_BY_OFFSET(latitude), EARLIEST_BY_OFFSET(longitude), LATEST_BY_OFFSET(latitude), LATEST_BY_OFFSET(longitude), 'KM') as distance
FROM coordinates_stream 
WINDOW TUMBLING (SIZE 90 SECONDS, GRACE PERIOD 90 DAYS)
GROUP BY carid
EMIT CHANGES;


-- CREATE STREAM TRACKS_STREAM AS SELECT carid, distance FROM TRACKS_TABLE EMIT CHANGES;

-- select max of distance per 180 sec < threshold
SELECT 
    carid, 
    MAX(distance) AS delta
FROM TRACKS_TABLE 
WINDOW TUMBLING (SIZE 180 SECONDS, GRACE PERIOD 90 DAYS)
GROUP BY carid
HAVING MAX(distance) < 0.1
EMIT CHANGES; 






---

SELECT 
    carid, 
    COLLECT_LIST(latitude) as latitudes, COLLECT_LIST(longitude) as longitudes
FROM coordinates_stream 
WINDOW TUMBLING (SIZE 90 SECONDS, GRACE PERIOD 90 DAYS)
GROUP BY carid
EMIT CHANGES;




SELECT 
    carid, 
    STDDEV_SAMP(latitude) as latitude_deviation, STDDEV_SAMP(longitude) as longitude_deviation
FROM coordinates_stream 
WINDOW TUMBLING (SIZE 90 SECONDS, GRACE PERIOD 90 DAYS)
GROUP BY carid
EMIT CHANGES;



-- https://ru.wikipedia.org/wiki/%D0%A1%D1%80%D0%B5%D0%B4%D0%BD%D0%B5%D0%BA%D0%B2%D0%B0%D0%B4%D1%80%D0%B0%D1%82%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%BE%D0%B5_%D0%BE%D1%82%D0%BA%D0%BB%D0%BE%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5
-- Use it
SELECT 
    carid
FROM coordinates_stream 
    WINDOW TUMBLING (SIZE 90 SECONDS, GRACE PERIOD 90 DAYS)
GROUP BY carid
HAVING 
    STDDEV_SAMP(CAST (latitude * 100000000000 AS bigint)) < 10 AND 
    STDDEV_SAMP(CAST (longitude * 100000000000 AS bigint)) < 10
EMIT CHANGES;








SELECT 
    carid, 
    avg(latitude) as latitude_avg, avg(longitude) as longitude_avg
FROM coordinates_stream 
WINDOW TUMBLING (SIZE 90 SECONDS, GRACE PERIOD 90 DAYS)
GROUP BY carid
EMIT CHANGES;


SELECT 
    carid, 
    avg(latitude) as latitude_avg, avg(longitude) as longitude_avg
FROM coordinates_stream 
WINDOW TUMBLING (SIZE 90 SECONDS, GRACE PERIOD 90 DAYS)
GROUP BY carid
EMIT CHANGES;

```


# Initializing migrations from host
```
docker run -v $PWD/docker/ksqldb/migrations:/share/ksql-migrations confluentinc/ksqldb-server:0.22.0 ksql-migrations new-project /share/ksql-migrations http://host.docker.internal:8088
```

# Apply migration from host
```
docker run -v $PWD/docker/ksqldb/migrations:/share/ksql-migrations confluentinc/ksqldb-server:0.22.0 ksql-migrations --config-file /share/ksql-migrations/ksql-migrations.properties initialize-metadata

docker run -v $PWD/docker/ksqldb/migrations:/share/ksql-migrations confluentinc/ksqldb-server:0.22.0 ksql-migrations --config-file /share/ksql-migrations/ksql-migrations.properties apply --all
```
