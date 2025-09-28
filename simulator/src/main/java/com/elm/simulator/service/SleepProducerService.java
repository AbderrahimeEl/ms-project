package com.elm.simulator.service;

import com.elm.simulator.model.SleepEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SleepProducerService {

    private final KafkaTemplate<String, SleepEvent> kafkaTemplate;
    private Iterator<SleepEvent> eventIterator;

    @Value("${dataset.sleep_data}")
    private String filePath;

    public SleepProducerService(KafkaTemplate<String, SleepEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    private void init() {
        loadCsv(filePath);
    }

    private void loadCsv(String csvFilePath) {
        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            List<SleepEvent> events = Files.lines(resource.getFile().toPath())
                    .skip(1)
                    .map(line -> {
                        String[] parts = line.split(",");
                        return new SleepEvent("123", parts[1], Integer.parseInt(parts[2]), parts[3]);
                    })
                    .collect(Collectors.toList());

            eventIterator = events.iterator();
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }

    @Scheduled(fixedRate = 60000) // Send every 1 m
    public void sendNextEvent() {
        if (eventIterator.hasNext()) {
            SleepEvent event = eventIterator.next();
            kafkaTemplate.send("sleep-events", event.getUserId(), event);
            System.out.println("Sent sleep event: " + event);
        }
    }
}