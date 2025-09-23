package com.elm.simulator.service;

import com.elm.simulator.model.HeartrateEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class HeartrateProducerService {
    private final KafkaTemplate<String, HeartrateEvent> kafkaTemplate;

    public HeartrateProducerService(KafkaTemplate<String, HeartrateEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceFromCsv(String csvFilePath) {

        ClassPathResource resource = new ClassPathResource(csvFilePath);
        try (BufferedReader reader = Files.newBufferedReader(resource.getFile().toPath())) {
            reader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] parts = line.split(",");
                        return new HeartrateEvent(
                                parts[0],
                                parts[1],
                                Integer.parseInt(parts[2])
                        );
                    })
                    .forEach(event -> {
                        kafkaTemplate.send("heartrate-events", event.getUserId(), event);
                        System.out.println("Sent: " + event);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }
}
