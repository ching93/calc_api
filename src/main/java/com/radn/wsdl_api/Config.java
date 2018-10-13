package com.radn.wsdl_api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Config {
    @Bean
    public Jaxb2Marshaller marchaller() {
        Jaxb2Marshaller marchaller = new Jaxb2Marshaller();
        marchaller.setContextPath("com.radn.domainClasses");
        return marchaller;
    }
    @Bean
    public SendReceiveClient client(Jaxb2Marshaller marchaller) {
        SendReceiveClient client = new SendReceiveClient();
        client.setDefaultUri("http://www.dneonline.com/calculator.asmx");
        client.setMarshaller(marchaller);
        client.setUnmarshaller(marchaller);
        return client;
    }
}
