# Create the topic
#! /bin/bash
kafka-topics.sh --create --topic web-domains --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

kafka-topics.sh --create --topic active-web-domains --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092

#kafka-topics.sh --create --topic active- --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092




