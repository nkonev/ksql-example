#!/bin/bash

format_done=/bitnami/kafka/format_done

if [[ ! -f $format_done ]]; then
  echo "Will format kafka-storage for KRaft"

#  mkdir -p /opt/bitnami/kafka/conf
#  touch /bitnami/kafka/config/kafka.properties

  # KRaft required step: Format the storage directory with a new cluster ID
  kafka_storage=/opt/bitnami/kafka/bin/kafka-storage.sh
  $kafka_storage format --ignore-formatted -t $($kafka_storage random-uuid) -c /opt/bitnami/kafka/config/kraft/server.properties

  touch $format_done
else
  echo "kafka-storage is already formatted, skipping"
fi

echo "Starting original run script"
exec /opt/bitnami/scripts/kafka/entrypoint.sh /opt/bitnami/kafka/bin/kafka-server-start.sh /opt/bitnami/kafka/config/kraft/server.properties
# ./bin/kafka-server-start.sh ./config/kraft/server.properties
#sleep 1000000