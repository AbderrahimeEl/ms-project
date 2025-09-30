package com.elm.simulator.service;

import com.elm.simulator.model.StepEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
            LocalDateTime startTime = LocalDateTime.now();
            AtomicInteger counter = new AtomicInteger(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");
            List<StepEvent> events = Files.lines(resource.getFile().toPath())
                    .skip(1)
                    .map(line -> {
                        LocalDateTime eventTime = startTime.plusMinutes(counter.getAndIncrement());
                        String[] parts = line.split(",");
                        return new StepEvent("123",eventTime.format(formatter), Integer.parseInt(parts[2]));
                    })
                    .collect(Collectors.toList());

            eventIterator = events.iterator();
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }
    @Scheduled(fixedRate = 60000)
    public void sendNextEvent() {
        if (eventIterator.hasNext()) {
            StepEvent event = eventIterator.next();
            kafkaTemplate.send("min-steps-events", event.getUserId(), event);
            System.out.println("Sent: " + event);
        }
    }
}
