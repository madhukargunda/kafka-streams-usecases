# Create the topic
#! /bin/bash
kafka-topics.sh --create --topic toyata-car-sales --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092 --config retention.ms=300000

kafka-topics.sh --create --topic texas-toyata-car-sales --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092 --config retention.ms=300000
