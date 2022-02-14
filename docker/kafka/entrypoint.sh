#!/bin/bash

. /opt/bitnami/scripts/libfile.sh
. /opt/bitnami/scripts/liblog.sh
. /opt/bitnami/scripts/libkafka.sh

config_done=/var/lib/kafka-data/config_done

if [[ ! -f $config_done ]]; then
  echo "Will edit config"
  # altering config
  # KAFKA_CONF_FILE is from /opt/bitnami/scripts/kafka-env.sh
  export KAFKA_CONF_FILE=/opt/bitnami/kafka/config/kraft/server.properties

  kafka_configure_from_environment_variables

  touch $config_done
else
  echo "config is already edited, skipping"
fi


format_done=/var/lib/kafka-data/format_done

if [[ ! -f $format_done ]]; then
  echo "Will format kafka-storage for KRaft"

  # KRaft required step: Format the storage directory with a new cluster ID
  kafka_storage=/opt/bitnami/kafka/bin/kafka-storage.sh
  $kafka_storage format --ignore-formatted -t $($kafka_storage random-uuid) -c /opt/bitnami/kafka/config/kraft/server.properties

  touch $format_done
else
  echo "kafka-storage is already formatted, skipping"
fi

export PATH=${PATH}:/opt/bitnami/kafka/bin

echo "Starting non-original run script"
exec /opt/bitnami/kafka/bin/kafka-server-start.sh /opt/bitnami/kafka/config/kraft/server.properties