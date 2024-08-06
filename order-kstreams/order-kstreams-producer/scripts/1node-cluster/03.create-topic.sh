# Create the topic
#! /bin/bash
kafka-topics.sh --create --topic orders --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

