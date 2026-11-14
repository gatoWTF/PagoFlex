package com.pagoflex.compromisos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CompromisosServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompromisosServiceApplication.class, args);
    }
}
