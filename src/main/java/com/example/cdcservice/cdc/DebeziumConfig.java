package com.example.cdcservice.cdc;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class DebeziumConfig {

    @Bean
    public DebeziumEngine<ChangeEvent<String, String>> debeziumEngine(
            @Value("${debezium.postgres.hostname}") String hostname,
            @Value("${debezium.postgres.port}") String port,
            @Value("${debezium.postgres.user}") String username,
            @Value("${debezium.postgres.password}") String password,
            @Value("${debezium.postgres.dbname}") String dbname,
            @Value("${debezium.postgres.server.name}") String serverName,
            @Value("${debezium.postgres.plugin.name}") String pluginName,
            @Value("${debezium.postgres.slot.name}") String slotName,
            @Value("${debezium.postgres.table.include.list}") String tableIncludeList,
            DebeziumChangeListener listener
    ) {

        io.debezium.config.Configuration config =
                io.debezium.config.Configuration.create()
                        .with("name", "pg-connector")
                        .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                        .with("database.hostname", hostname)
                        .with("database.port", port)
                        .with("database.user", username)
                        .with("database.password", password)
                        .with("database.dbname", dbname)
                        .with("database.server.name", serverName)
                        .with("plugin.name", pluginName)
                        .with("slot.name", slotName)
                        .with("topic.prefix", "cdc")
                        .with("table.include.list", tableIncludeList)
                        .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                        .with("offset.storage.file.filename",
                                "C:/Users/PraveenRao/tarento/logs/offsets.dat")
                        .with("offset.flush.interval.ms", "1000")
                        .build();

        return DebeziumEngine.create(Json.class)
                .using(config.asProperties())
                .notifying(listener::handleChange)
                .build();
    }

    @Bean
    public Executor debeziumExecutor(DebeziumEngine<ChangeEvent<String, String>> engine) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);
        log.info("Debezium Engine started...");
        return executor;
    }
}
