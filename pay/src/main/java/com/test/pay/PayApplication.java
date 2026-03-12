package com.test.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

// pay 서비스의 시작점이다.
// @ConfigurationPropertiesScan을 켜서 application.yaml의 설정 묶음을
// KafkaTopicProperties 같은 객체에 자동으로 바인딩할 수 있게 한다.
@ConfigurationPropertiesScan
@SpringBootApplication
public class PayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayApplication.class, args);
	}

}