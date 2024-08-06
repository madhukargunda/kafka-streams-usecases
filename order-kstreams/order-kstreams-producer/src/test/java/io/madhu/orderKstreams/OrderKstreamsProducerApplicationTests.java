package io.madhu.orderKstreams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.madhu.orderKstreams.model.Order;
import io.madhu.orderKstreams.service.OrderDataProduceService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Slf4j
class OrderKstreamsProducerApplicationTests {

	@Autowired
	private OrderDataProduceService orderDataProduceService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() throws IOException {
		List<Order> orders = orderDataProduceService.buildOrders();
		byte[] bytes = objectMapper.writeValueAsBytes(orders);
		List<Order> list = objectMapper.readValue(bytes, new TypeReference<List<Order>>(){});
		log.info("Type Order List value {}",list);
	}
}
