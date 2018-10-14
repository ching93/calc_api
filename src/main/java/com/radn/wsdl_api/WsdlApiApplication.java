package com.radn.wsdl_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ws.soap.SoapMessage;

@EnableScheduling
@EnableCaching
@EnableTransactionManagement
@SpringBootApplication
public class WsdlApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsdlApiApplication.class, args);
    }
}
