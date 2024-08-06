#!/bin/bash

# Kafka topic and broker configuration
KAFKA_TOPIC="toyota-car-sales"
KAFKA_BROKER="localhost:9092"

# Expanded sets of random values
DEALER_IDS=("100010" "100050" "100060" "100030" "100070" "100080" "100090")
YEARS=("2020" "2021" "2022")
MODELS=("Corolla" "Highlander" "Sienna" "Camry")
DEALER_NAMES=("Price Motors" "The Car Guys" "Friendly Neighbour Toyota" "Gulf Toyota" "AutoHub" "Premier Auto Sales" "City Car Center")
STATES=("New York" "Texas" "California" "New Jersey" "Florida" "Illinois" "Ohio" "Nevada" "Arizona")

#Setting the environment variable
export KAFKA_HOME=/Users/madhu/work/Tools/kafka_2.13-3.6.1

# Function to generate a random UUID
generate_uuid() {
  # cat /proc/sys/kernel/random/uuid
  uuidgen
}

# Function to pick a random element from an array
pick_random() {
  local arr=("$@")
  echo "${arr[RANDOM % ${#arr[@]}]}"
}

# Function to generate a random timestamp
generate_timestamp() {
  date +"%Y%m%d%H%M%S"
}

# Function to generate a random price between a range
generate_price() {
  echo $(( ( RANDOM % 20000 ) + 10000 ))
}

# Function to generate JSON data
generate_data() {
  local dealer_id=$(pick_random "${DEALER_IDS[@]}")
  local year=$(pick_random "${YEARS[@]}")
  local model=$(pick_random "${MODELS[@]}")
  local dealer_name=$(pick_random "${DEALER_NAMES[@]}")
  local state=$(pick_random "${STATES[@]}")
  local price=$(generate_price)

  cat <<EOF
{
  "transactionId":"$(generate_uuid)",
  "make":"Toyota",
  "model":"$model",
  "year":"$year",
  "saleTimestamp":"$(generate_timestamp)",
  "dealerId":"$dealer_id",
  "dealerName":"$dealer_name",
  "state":"$state",
  "price":"$price"
}
EOF
}

# Infinite loop to send data continuously
while true; do
  # Generate unique data
  json_data=$(generate_data)
  # json_data=$(echo "$json_data" | tr -d '\n')
  #json_data=$(echo "$json_data" | sed ':a;N;$!ba;s/\n/ /g')
  #json_data=$(echo "$json_data" | sed ':a;N;$!ba;s/\n/ /g')
  #json_data=$(echo "$json_data" | sed -e ':a' -e 'N' -e '$!ba' -e 's/\n/ /g')

  # First, remove the newlines
  json_data=$(echo "$json_data" | tr -d '\n')

  # Then, remove extra spaces between JSON fields
  json_data=$(echo "$json_data" | sed 's/[[:space:]]\{2,\}//g')

  # Send the data to Kafka
  echo "$json_data"
  echo "$json_data" | kafka-console-producer.sh --broker-list $KAFKA_BROKER --topic $KAFKA_TOPIC

  # Wait for a second before sending the next message
  sleep 4
done
