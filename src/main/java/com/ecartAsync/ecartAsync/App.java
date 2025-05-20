package com.ecartAsync.ecartAsync;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ecartAsync")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}