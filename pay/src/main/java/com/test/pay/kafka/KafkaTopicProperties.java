package com.test.pay.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

// application.yaml의 app.kafka.topics.* 설정을 코드에서 묶어서 받기 위한 클래스다.
// 토픽이 많아질수록 @Value를 여러 군데에 흩뿌리는 것보다 이런 방식이 관리하기 쉽다.
@ConfigurationProperties(prefix = "app.kafka.topics")
public record KafkaTopicProperties(
	// app.kafka.topics.payment-created 값이 이 필드에 매핑된다.
	String paymentCreated
) {
}