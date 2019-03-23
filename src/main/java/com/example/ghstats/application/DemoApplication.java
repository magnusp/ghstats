package com.example.ghstats.application;

import com.example.ghstats.application.framework.InvokingWebClientProxyRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

@SpringBootApplication
@Import(InvokingWebClientProxyRegistrar.class)
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
