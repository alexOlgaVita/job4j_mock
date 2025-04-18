package ru.checkdev.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.Notify;

@Service
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

    public void put(final Notify notify) {
        kafkaTemplate.send(topic, notify);
    }
}
