package org.arvind.commerce.api.job;


import lombok.extern.slf4j.Slf4j;
import org.arvind.commerce.api.AssignmentApplication;
import org.arvind.commerce.api.dto.OrderEmailDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class EmailSenderAgent {

    @Scheduled(fixedRate = 500, initialDelay = 1000)
    public void runDumpProcess() throws IOException {
        OrderEmailDTO orderEmailDTO = AssignmentApplication.backupQueue.poll();
        //send email with order id and payment linlk

    }
}
