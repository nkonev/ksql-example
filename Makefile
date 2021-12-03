current_dir = $(shell pwd)
ksql_version = 0.22.0

.PHONY: migrate-init migrate

migrate-init:
	docker run --rm -v $(current_dir)/docker/ksqldb/migrations:/share/ksql-migrations confluentinc/ksqldb-server:$(ksql_version) ksql-migrations --config-file /share/ksql-migrations/ksql-migrations.properties initialize-metadata

migrate:
	docker run --rm -v $(current_dir)/docker/ksqldb/migrations:/share/ksql-migrations confluentinc/ksqldb-server:$(ksql_version) ksql-migrations --config-file /share/ksql-migrations/ksql-migrations.properties apply --all
