package com.radn.wsdl_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

public class Tasks {
    private int operationCounter = 0;

    public void checkOperations() {
        System.out.println("Checking oprations...");
        operationCounter = 0;
    }
}
