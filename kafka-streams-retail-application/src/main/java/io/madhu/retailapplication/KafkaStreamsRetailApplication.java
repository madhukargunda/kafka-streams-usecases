package io.madhu.retailapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
public class KafkaStreamsRetailApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamsRetailApplication.class, args);
	}
}
