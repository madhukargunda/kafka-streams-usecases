#start the kafka server
#! /bin/bash
#Check HashiCorp Valut is using the port in case if its not starting
#lsof -n -i :9092 | grep LISTEN

kafka-server-start.sh "$KAFKA_HOME/config/server-0.properties"