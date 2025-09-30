package com.elm.simulator.service;

import com.elm.simulator.model.HeartrateEvent;
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
public class HeartrateProducerService {

    private final KafkaTemplate<String, HeartrateEvent> kafkaTemplate;
    private Iterator<HeartrateEvent> eventIterator;


    @Value("${dataset.sec_heartrates}")
    private String filePath;

    public HeartrateProducerService(KafkaTemplate<String, HeartrateEvent> kafkaTemplate) {
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
            List<HeartrateEvent> events = Files.lines(resource.getFile().toPath())
                    .skip(1)
                    .map(line -> {
                        LocalDateTime eventTime = startTime.plusSeconds(5L * counter.getAndIncrement());
                        String[] parts = line.split(",");
                        return new HeartrateEvent("123",  eventTime.format(formatter), Integer.parseInt(parts[2]));
                    })
                    .collect(Collectors.toList());

            eventIterator = events.iterator();
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendNextEvent() {
        if (eventIterator.hasNext()) {
            HeartrateEvent event = eventIterator.next();
            kafkaTemplate.send("heartrate-events", event.getUserId(), event);
            System.out.println("Sent: " + event);
        }
    }
}
