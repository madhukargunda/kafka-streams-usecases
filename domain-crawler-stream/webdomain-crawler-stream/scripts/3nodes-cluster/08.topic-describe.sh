
#! /bin/bash
# Producer producing the csv file to kafka cluster

"$KAFKA_HOME/bin/kafka-topics.sh" --describe --topic test --bootstrap-server localhost:9092
