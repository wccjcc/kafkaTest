package com.test.insight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.kafka.annotation.EnableKafka;

// insight 서비스의 시작점이다.
// @EnableKafka로 @KafkaListener를 활성화하고,
// @ConfigurationPropertiesScan으로 토픽 설정 바인딩을 켠다.
@EnableKafka
@ConfigurationPropertiesScan
@SpringBootApplication
public class InsightApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsightApplication.class, args);
	}

}