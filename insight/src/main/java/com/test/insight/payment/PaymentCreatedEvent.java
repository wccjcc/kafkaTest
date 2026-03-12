package com.test.insight.payment;

import java.math.BigDecimal;
import java.time.Instant;

// Kafka에서 받아오는 결제 생성 이벤트 DTO다.
// producer 쪽 이벤트 스키마와 맞아야 역직렬화가 정상 동작한다.
public record PaymentCreatedEvent(
	String paymentId,
	String orderId,
	BigDecimal amount,
	Instant createdAt
) {
}