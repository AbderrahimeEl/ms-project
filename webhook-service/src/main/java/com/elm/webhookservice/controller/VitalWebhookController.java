package com.elm.webhookservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class VitalWebhookController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public VitalWebhookController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<String> receiveData(@RequestBody String payload) {
        System.out.println("📩 Received webhook data: " + payload);

        kafkaTemplate.send("vital-data", payload);

        return ResponseEntity.ok("Webhook received and sent to Kafka");
    }
}
