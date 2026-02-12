package ru.krik.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


import java.time.Duration;

//@Validated
@Data
@ConfigurationProperties("com")
public class KafkaProperties {
    @NotBlank
    //@Value("${com.itrum.test.kafka.request-topic}")
    String requestTopic = "notification-request";

    //@Value("${com.itrum.test.kafka.reply-topic}")
    @NotBlank
    String replyTopic = "notification-response";

    @NotNull
    @DurationMin(seconds = 10)
    @DurationMax(minutes = 2)
    //@Value("${com.itrum.test.kafka.reply-timeout}")
    Duration replyTimeout = Duration.ofSeconds(30);
}
