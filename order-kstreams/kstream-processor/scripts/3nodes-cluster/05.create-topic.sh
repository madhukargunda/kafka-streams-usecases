# Create the topic
#! /bin/bash

# Create ka
kafka-topics.sh --create --topic invoice --partitions 5 --replication-factor 3 --bootstrap-server localhost:9092

