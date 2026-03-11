package com.test.pay.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private final String paymentTopic;

	public PaymentProducer(
		KafkaTemplate<String, String> kafkaTemplate,
		ObjectMapper objectMapper,
		@Value("${app.kafka.payment-topic}") String paymentTopic
	) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
		this.paymentTopic = paymentTopic;
	}

	public void send(PaymentCreatedEvent event) {
		try {
			String payload = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(paymentTopic, event.paymentId(), payload);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Failed to serialize payment event", e);
		}
	}
}
