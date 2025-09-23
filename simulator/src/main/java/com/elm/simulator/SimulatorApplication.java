package com.elm.simulator;


import com.elm.simulator.service.HeartrateProducerService;
import com.elm.simulator.service.StepsProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimulatorApplication implements CommandLineRunner {
    private final StepsProducerService stepsProducerService;
    private final HeartrateProducerService heartrateProducerService;

    @Value("${dataset.h_steps}")
    private String stepsFilePath;

    @Value("${dataset.sec_heartrates}")
    private String heartratesFilePath;

    public SimulatorApplication(StepsProducerService stepsProducerService, HeartrateProducerService heartrateProducerService) {
        this.stepsProducerService = stepsProducerService;
        this.heartrateProducerService = heartrateProducerService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SimulatorApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
       new Thread(()-> stepsProducerService.produceFromCsv(stepsFilePath)).start();
       new Thread(() -> {heartrateProducerService.produceFromCsv(heartratesFilePath);}).start();
    }
}
