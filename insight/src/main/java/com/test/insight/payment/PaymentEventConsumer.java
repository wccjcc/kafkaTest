package com.test.insight.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {

	private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);

	private final ObjectMapper objectMapper;

	public PaymentEventConsumer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = "${app.kafka.payment-topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void consume(String payload) {
		try {
			PaymentCreatedEvent event = objectMapper.readValue(payload, PaymentCreatedEvent.class);
			log.info(
				"Consumed payment event paymentId={}, orderId={}, amount={}, createdAt={}",
				event.paymentId(),
				event.orderId(),
				event.amount(),
				event.createdAt()
			);
		} catch (JsonProcessingException e) {
			log.error("Failed to parse payment event payload={}", payload, e);
		}
	}
}
