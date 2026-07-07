package com.vertex;

import com.vertex.config.VertexProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(VertexProperties.class)
@EnableScheduling
public class VertexApplication {
    public static void main(String[] args) {
        SpringApplication.run(VertexApplication.class, args);
    }
}

