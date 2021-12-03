#!/bin/bash

format_done=/var/lib/kafka/data/format_done

if [[ ! -f $format_done ]]; then
  echo "Will format kafka-storage for KRaft"

  # Docker workaround: Remove check for KAFKA_ZOOKEEPER_CONNECT parameter
  sed -i '/KAFKA_ZOOKEEPER_CONNECT/d' /etc/confluent/docker/configure

  # Docker workaround: Ignore cub zk-ready
  sed -i 's/cub zk-ready/echo ignore zk-ready/' /etc/confluent/docker/ensure

  # KRaft required step: Format the storage directory with a new cluster ID
  echo "kafka-storage format --ignore-formatted -t $(kafka-storage random-uuid) -c /etc/kafka/kafka.properties" >> /etc/confluent/docker/ensure

  touch $format_done
else
  echo "kafka-storage is already formatted, skipping"
fi

echo "Starting original confluent run script"
exec /etc/confluent/docker/run