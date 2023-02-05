package com.littlepay.taponoff.application;

import com.littlepay.taponoff.domain.service.FareCalculatorService;
import com.littlepay.taponoff.domain.service.FareCalculatorServiceImpl;
import com.littlepay.taponoff.infrastructure.FareRepository;
import com.littlepay.taponoff.infrastructure.TapRepository;
import com.littlepay.taponoff.infrastructure.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Objects;

@SpringBootApplication(scanBasePackages = "com.littlepay.taponoff")
@EnableJpaRepositories("com.littlepay.taponoff")
@EntityScan("com.littlepay.taponoff")
public class Application implements CommandLineRunner {

    @Autowired
    FareCalculatorService fareCalculatorService;
    @Autowired
    private TapRepository tapRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private FareRepository fareRepository;

    @Bean
    public FareCalculatorService fareCalculatorService() {
        return new FareCalculatorServiceImpl(tapRepository, tripRepository, fareRepository);
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        String filename = Objects.requireNonNull(getClass().getClassLoader().getResource("data.csv")).getPath();
        fareCalculatorService.calculateFaresFrom(filename);
    }
}
