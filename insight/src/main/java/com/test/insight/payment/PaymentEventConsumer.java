package com.test.insight.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// payment.created 토픽의 메시지를 구독하는 consumer다.
// 지금 예제에서는 받은 이벤트를 로그로만 출력하지만,
// 실제 프로젝트에서는 여기서 저장, 통계 적재, 알림 발송 같은 후속 작업을 처리한다.
@Component
public class PaymentEventConsumer {

	private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);

	@KafkaListener(
		topics = "${app.kafka.topics.payment-created}",
		groupId = "${spring.kafka.consumer.group-id}",
		containerFactory = "paymentKafkaListenerContainerFactory"
	)
	public void consume(PaymentCreatedEvent event) {
		// KafkaConfig에서 JSON -> PaymentCreatedEvent 변환이 끝난 상태로 들어오므로,
		// 이 메서드는 문자열 파싱 없이 바로 비즈니스 로직에 집중할 수 있다.
		log.info(
			"Consumed payment event paymentId={}, orderId={}, amount={}, createdAt={}",
			event.paymentId(),
			event.orderId(),
			event.amount(),
			event.createdAt()
		);
	}
}