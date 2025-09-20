package com.elm.simulator;

import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class SimulatorApplication implements CommandLineRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final Random random = new Random();
    private final int[] users = {1, 2, 3, 4, 5};

    public static void main(String[] args) {
        SpringApplication.run(SimulatorApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
        while (true) {
            LocalTime now = LocalTime.now();
            int hour = now.getHour();

            for (int user : users) {
                JSONObject data = new JSONObject();
                data.put("user_id", user);
                data.put("timestamp", System.currentTimeMillis());

                int baseSteps = switch (user) {
                    case 1 -> 5;
                    case 2 -> 2;
                    case 3 -> 10;
                    case 4 -> 3;
                    case 5 -> 7;
                    default -> 0;
                };

                double dayFactor = 1.0;
                if (hour >= 23 || hour <= 6) dayFactor = 0.2;
                else if (hour >= 7 && hour <= 9) dayFactor = 0.5;
                else if (hour >= 17 && hour <= 20) dayFactor = 1.5;

                int burst = random.nextInt(30);
                int steps = (int)((baseSteps + burst) * dayFactor);
                data.put("steps", steps);

                kafkaTemplate.send("my-topic", Integer.toString(user), data.toString());
            }

            TimeUnit.SECONDS.sleep(10);
        }
    }
}
