# Create the topic
#! /bin/bash
kafka-topics.sh --create --topic greetings --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

kafka-topics.sh --create --topic words-stream --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

kafka-topics.sh --create --topic words-count --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

kafka-topics.sh --create --topic greeting-upper --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

kafka-topics.sh --create --topic order --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092


