package com.pm.patient_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PatientServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(PatientServiceApplication.class);

    public static void main(String[] args) {

       var context = SpringApplication.run(PatientServiceApplication.class, args);

        String bootstrapServer = context.getEnvironment().getProperty("spring.kafka.bootstrap-servers");
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        log.info("KAFKA BOOTSTRAP SERVER BEING USED: {}", bootstrapServer);
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

}
