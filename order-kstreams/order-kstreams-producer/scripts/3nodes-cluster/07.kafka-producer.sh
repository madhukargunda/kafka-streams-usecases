
#! /bin/bash
# Producer producing the csv file to kafka cluster

kafka-console-producer.sh --topic test --bootstrap-server localhost:9092 < ../csv/sample.csv
