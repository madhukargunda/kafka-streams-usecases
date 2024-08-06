
#! /bin/bash
# Producer producing the csv file to kafka cluster
kafka-console-producer.sh --topic toyata-car-sales --broker-list localhost:9092 < ../data/car-sales.json

# kafka-console-producer.sh --topic toyata-car-sales --broker-list localhost:9092 < ../data/car-sales.json --property parse.key=true --property key.separator=: