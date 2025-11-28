package com.example.cdcservice.cdc;

import io.debezium.engine.ChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DebeziumChangeListener {
    public void handleChange(ChangeEvent<String, String> event) {
        log.info("CDC Event: {}", event.toString());
    }
}
