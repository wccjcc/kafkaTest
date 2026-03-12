package com.test.pay.payment;

import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// Kafka 실습 흐름의 HTTP 진입점이다.
// 실제 프로젝트라면 컨트롤러 -> 서비스 -> 저장 -> 이벤트 발행 구조로 가겠지만,
// 지금 예제는 Kafka 흐름을 보기 위해 컨트롤러에서 바로 이벤트를 만든다.
@RestController
public class PaymentController {

	private final PaymentProducer paymentProducer;

	public PaymentController(PaymentProducer paymentProducer) {
		this.paymentProducer = paymentProducer;
	}

	@PostMapping("/payments")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public PaymentCreatedEvent createPayment(@RequestBody PaymentRequest request) {
		// paymentId와 createdAt은 서버에서 직접 만든다.
		// 이렇게 만든 이벤트를 Kafka에 발행하고, 응답 본문으로도 그대로 돌려준다.
		PaymentCreatedEvent event = new PaymentCreatedEvent(
			UUID.randomUUID().toString(),
			request.orderId(),
			request.amount(),
			Instant.now()
		);
		paymentProducer.send(event);
		return event;
	}
}