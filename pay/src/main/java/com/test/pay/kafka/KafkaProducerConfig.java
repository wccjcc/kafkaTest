package com.test.pay.kafka;

import com.test.pay.payment.PaymentCreatedEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

// producer 쪽 Kafka Bean을 명시적으로 등록하는 설정 클래스다.
// 이 클래스의 목적은 PaymentCreatedEvent를 직접 Kafka로 보내기 위한
// ProducerFactory와 KafkaTemplate을 만드는 것이다.
@Configuration
public class KafkaProducerConfig {

	@Bean
	public ProducerFactory<String, PaymentCreatedEvent> paymentProducerFactory(KafkaProperties kafkaProperties) {
		// application.yaml의 spring.kafka.* 값을 기반으로
		// Spring Boot가 계산해둔 기본 producer 속성을 먼저 가져온다.
		Map<String, Object> properties = new HashMap<>(kafkaProperties.buildProducerProperties());

		// Kafka 레코드의 key는 문자열로 직렬화한다.
		// 현재 예제에서는 paymentId를 key로 사용한다.
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		// Kafka 레코드의 value는 PaymentCreatedEvent DTO를
		// JSON으로 바꿔서 보내기 위해 JsonSerializer를 사용한다.
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		// Spring 전용 타입 헤더를 추가하지 않는다.
		// 지금 예제는 단순한 JSON payload 흐름을 학습하는 것이 목적이므로
		// 불필요한 헤더를 빼고 producer와 consumer 구조를 단순하게 유지한다.
		properties.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

		// 위에서 만든 설정값으로 ProducerFactory를 생성한다.
		// 실제 Kafka producer 인스턴스는 이 팩토리를 통해 만들어진다.
		return new DefaultKafkaProducerFactory<>(properties);
	}

	@Bean
	public KafkaTemplate<String, PaymentCreatedEvent> kafkaTemplate(
		ProducerFactory<String, PaymentCreatedEvent> paymentProducerFactory
	) {
		// 비즈니스 코드에서 바로 사용할 KafkaTemplate을 만든다.
		// 이제 PaymentProducer는 ObjectMapper로 직접 JSON 변환하지 않고
		// DTO를 그대로 send()에 넘기면 된다.
		return new KafkaTemplate<>(paymentProducerFactory);
	}
}