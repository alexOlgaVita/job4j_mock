package ru.checkdev.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.Notify;

@Slf4j
@Service
public class NotificationService {

    private final TemplateService templates;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${topic.notification}")
    private String topic;

    @Value("${retries.notification}")
    private int retries;

    @Value("${delay.notification}")
    private long delay;

    @Autowired
    public NotificationService(final TemplateService templates, KafkaTemplate<String, Object> kafkaTemplate) {
        this.templates = templates;
        this.kafkaTemplate = kafkaTemplate;
    }

    public interface Act<T> {
        T exec() throws Exception;
    }

    public <R> R exec(Act<R> act, R defVal) {
        int i = 0;
        do {
            i++;
            try {
                return act.exec();
            } catch (Exception e) {
                log.error("Attempt {} failed: {}", i, e.getMessage(), e);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.error("Thread interrupted during sleep", ie);
                }
            }
        } while (i < retries);
        return defVal;
    }

    public void put(final Notify notify) {
        Act<String> permitExc = () -> {
            kafkaTemplate.send(topic, notify);
            return "Notify was sent";
        };
        log.info(exec(permitExc, "Notify was not sent"));
    }
}
