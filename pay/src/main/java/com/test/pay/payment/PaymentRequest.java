package com.test.pay.payment;

import java.math.BigDecimal;

public record PaymentRequest(
	String orderId,
	BigDecimal amount
) {
}
