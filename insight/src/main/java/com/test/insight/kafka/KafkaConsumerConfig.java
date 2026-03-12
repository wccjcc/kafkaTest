package com.test.insight.kafka;

import com.test.insight.payment.PaymentCreatedEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

// consumer 쪽 Kafka Bean을 명시적으로 등록하는 설정 클래스다.
// 이 클래스의 핵심 목적은 Kafka에서 받은 JSON 메시지를
// PaymentCreatedEvent DTO로 자동 변환해서 listener에 넘기는 것이다.
@Configuration
public class KafkaConsumerConfig {

	@Bean
	public ConsumerFactory<String, PaymentCreatedEvent> paymentConsumerFactory(KafkaProperties kafkaProperties) {
		// application.yaml의 spring.kafka.consumer.* 값을 기반으로
		// Spring Boot가 계산한 기본 consumer 속성을 먼저 가져온다.
		Map<String, Object> properties = new HashMap<>(kafkaProperties.buildConsumerProperties());

		// Kafka 레코드의 key는 문자열로 역직렬화한다.
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		// Kafka 레코드의 value는 JSON을 DTO로 바꾸기 위해 JsonDeserializer를 사용한다.
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		// 타입 헤더가 없어도 이 consumer는 PaymentCreatedEvent로 읽어야 한다는 기본 타입을 지정한다.
		// 즉, 들어오는 JSON payload를 이 클래스 구조로 매핑하라는 뜻이다.
		properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, PaymentCreatedEvent.class.getName());

		// producer 쪽에서 Spring 타입 헤더를 보내지 않도록 설정했으므로,
		// consumer도 헤더를 기대하지 않고 기본 타입으로 읽도록 맞춘다.
		properties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

		// 아무 클래스나 역직렬화하지 않도록 신뢰 패키지를 제한한다.
		// 지금 예제에서는 PaymentCreatedEvent가 있는 패키지만 허용한다.
		properties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.test.insight.payment");

		// 위에서 만든 설정값으로 ConsumerFactory를 생성한다.
		// 실제 Kafka consumer 인스턴스는 이 팩토리를 통해 만들어진다.
		return new DefaultKafkaConsumerFactory<>(properties);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PaymentCreatedEvent> paymentKafkaListenerContainerFactory(
		ConsumerFactory<String, PaymentCreatedEvent> paymentConsumerFactory
	) {
		// @KafkaListener 메서드를 실제로 실행해주는 컨테이너 팩토리를 만든다.
		// Kafka에서 poll한 메시지를 listener 메서드로 연결해주는 중간 실행기라고 보면 된다.
		ConcurrentKafkaListenerContainerFactory<String, PaymentCreatedEvent> factory =
			new ConcurrentKafkaListenerContainerFactory<>();

		// 위에서 만든 PaymentCreatedEvent 전용 consumer factory를 연결한다.
		factory.setConsumerFactory(paymentConsumerFactory);

		// 지금은 최소 설정만 두었고,
		// 나중에 동시성, 에러 핸들러, 재시도, DLT 같은 운영 옵션을 여기서 확장하면 된다.
		return factory;
	}
}