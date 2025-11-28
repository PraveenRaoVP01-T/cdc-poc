package com.example.cdcservice.cdc;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class DebeziumStart {
    private final DebeziumEngine<ChangeEvent<String, String>> engine;

    public DebeziumStart(DebeziumEngine<ChangeEvent<String, String>> engine) {
        this.engine = engine;
    }

    @PostConstruct
    public void start() {
        Executors.newSingleThreadExecutor().submit(engine);
        System.out.println("Debezium Engine Started...");
    }
}
