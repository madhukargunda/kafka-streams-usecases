
# kafka console consumer consume the data shared by producer
# shellcheck disable=SC1128
#! /bin/bash
kafka-console-consumer.sh --topic test --bootstrap-server localhost:9092 --from-beginning
