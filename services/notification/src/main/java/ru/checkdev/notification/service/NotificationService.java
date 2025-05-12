package ru.checkdev.notification.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.Notify;

@Service
@Slf4j
public class NotificationService {

    private final TemplateService templates;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${topic.notification}")
    private String topic;

    @Autowired
    public NotificationService(final TemplateService templates, KafkaTemplate<String, Object> kafkaTemplate) {
        this.templates = templates;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Retry(name = "tgAuthRetry")
    @CircuitBreaker(name = "tgAuthCircuitBreaker", fallbackMethod = "fallbackSend")
    public Mono<Object> put(final Notify notify) {
        kafkaTemplate.send(topic, notify);
        return null;
    }

    public Mono<Object> fallbackSend(Notify notify, Throwable throwable) {
        log.error("sent tо topic-kafka {} failed, fallback triggered: {}", topic, throwable.getMessage());
        return Mono.empty();
    }
}
