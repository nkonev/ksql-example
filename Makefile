ksql_version = 0.22.0

.PHONY: migrate-init migrate

migrate-init:
	docker exec -t ksqldb ksql-migrations --config-file /share/ksql-migrations/ksql-migrations.properties initialize-metadata

migrate:
	docker exec -t ksqldb ksql-migrations --config-file /share/ksql-migrations/ksql-migrations.properties apply --all
