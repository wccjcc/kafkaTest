package com.test.insight.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

// application.yaml의 app.kafka.topics.* 설정을 코드에서 묶어서 받기 위한 클래스다.
// producer와 consumer가 같은 토픽명을 바라보도록 맞출 때 유용하다.
@ConfigurationProperties(prefix = "app.kafka.topics")
public record KafkaTopicProperties(
	// app.kafka.topics.payment-created 값이 이 필드에 매핑된다.
	String paymentCreated
) {
}