package com.elm.simulator.service;

import com.elm.simulator.model.StepEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MinStepsProducerService {
    private final KafkaTemplate<String, StepEvent> kafkaTemplate;
    private Iterator<StepEvent> eventIterator;
    @Value("${dataset.min_steps}")
    private String filePath;

    public MinStepsProducerService(KafkaTemplate<String, StepEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @PostConstruct
    private void init() {
        loadCsv(filePath);
    }
    private void loadCsv(String csvFilePath) {
        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            List<StepEvent> events = Files.lines(resource.getFile().toPath())
                    .skip(1)
                    .map(line -> {
                        String[] parts = line.split(",");
                        return new StepEvent(parts[0], parts[1], Integer.parseInt(parts[2]));
                    })
                    .collect(Collectors.toList());

            eventIterator = events.iterator();
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }
    @Scheduled(fixedRate = 30000)
    public void sendNextEvent() {
        if (eventIterator.hasNext()) {
            StepEvent event = eventIterator.next();
            kafkaTemplate.send("heartrate-events", event.getUserId(), event);
            System.out.println("Sent: " + event);
        }
    }
}
