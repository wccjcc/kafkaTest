package com.test.pay.payment;

import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

	private final PaymentProducer paymentProducer;

	public PaymentController(PaymentProducer paymentProducer) {
		this.paymentProducer = paymentProducer;
	}

	@PostMapping("/payments")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public PaymentCreatedEvent createPayment(@RequestBody PaymentRequest request) {
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
