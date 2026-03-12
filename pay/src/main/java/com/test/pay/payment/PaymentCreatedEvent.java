package com.test.pay.payment;

import java.math.BigDecimal;
import java.time.Instant;

// 결제 생성 사실을 나타내는 Kafka 이벤트 DTO다.
// producer와 consumer가 같은 구조를 공유해야 정상적으로 직렬화와 역직렬화가 된다.
public record PaymentCreatedEvent(
	String paymentId,
	String orderId,
	BigDecimal amount,
	Instant createdAt
) {
}