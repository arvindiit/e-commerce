package org.arvind.commerce.api;

import org.arvind.commerce.api.dto.OrderEmailDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.LinkedList;
import java.util.Queue;

@SpringBootApplication
@EnableScheduling
public class AssignmentApplication {

    public static Queue<OrderEmailDTO> backupQueue;
    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
        backupQueue = new LinkedList<>();
    }

}