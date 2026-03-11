package com.test.insight.payment;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentCreatedEvent(
	String paymentId,
	String orderId,
	BigDecimal amount,
	Instant createdAt
) {
}
