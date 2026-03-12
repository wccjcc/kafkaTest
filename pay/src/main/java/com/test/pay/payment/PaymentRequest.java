package com.test.pay.payment;

import java.math.BigDecimal;

// /payments 요청 시 클라이언트가 보내는 요청 본문이다.
public record PaymentRequest(
	String orderId,
	BigDecimal amount
) {
}