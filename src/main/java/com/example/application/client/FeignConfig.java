package com.example.application.client;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.example.application.client", clients = {MFCRInfoClient.class})
public class FeignConfig {
}
