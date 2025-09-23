package com.elm.simulator.service;

import com.elm.simulator.model.StepEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

@Service
public class StepsProducerService {
    private final KafkaTemplate<String, StepEvent> kafkaTemplate;
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");

    public StepsProducerService(KafkaTemplate<String, StepEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceFromCsv(String csvFilePath) {

        ClassPathResource resource = new ClassPathResource(csvFilePath);

        try (BufferedReader reader = Files.newBufferedReader(resource.getFile().toPath())) {
            reader.lines()
                    .skip(1)
                    .map(line -> {
                        String[] parts = line.split(",");
                        return new StepEvent(
                                parts[0],
                                parts[1],
                                Integer.parseInt(parts[2])
                        );
                    })
                    .forEach(event -> {
                        kafkaTemplate.send("steps-events", event.getUserId(), event);
                        System.out.println("Sent: " + event);
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }
}
