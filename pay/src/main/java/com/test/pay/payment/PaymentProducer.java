package com.test.pay.payment;

import com.test.pay.kafka.KafkaTopicProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

// Kafka로 결제 생성 이벤트를 발행하는 역할만 담당하는 컴포넌트다.
// 토픽 이름, 직렬화 방식 같은 Kafka 세부사항을 이 클래스 안으로 감춘다.
@Component
public class PaymentProducer {

	private final KafkaTemplate<String, PaymentCreatedEvent> kafkaTemplate;
	private final KafkaTopicProperties kafkaTopicProperties;

	public PaymentProducer(
		KafkaTemplate<String, PaymentCreatedEvent> kafkaTemplate,
		KafkaTopicProperties kafkaTopicProperties
	) {
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaTopicProperties = kafkaTopicProperties;
	}

	public void send(PaymentCreatedEvent event) {
		// paymentId를 key로 사용한다.
		// 같은 key를 쓰는 메시지는 같은 파티션으로 갈 가능성이 높아서
		// 이벤트 순서를 묶어서 다루고 싶을 때 도움이 된다.
		// value는 DTO 그대로 넘기고, 실제 JSON 변환은 KafkaConfig에서 처리한다.
		kafkaTemplate.send(kafkaTopicProperties.paymentCreated(), event.paymentId(), event);
	}
}