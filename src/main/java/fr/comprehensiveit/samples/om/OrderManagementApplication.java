package fr.comprehensiveit.samples.om;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class OrderManagementApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(OrderManagementApplication.class, args);

    }

}