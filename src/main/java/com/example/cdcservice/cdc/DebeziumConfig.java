package com.example.cdcservice.cdc;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class DebeziumConfig {

    @Value("${debezium.postgres.hostname}")
    private String hostname;

    @Value("${debezium.postgres.port}")
    private String port;

    @Value("${debezium.postgres.user}")
    private String username;

    @Value("${debezium.postgres.password}")
    private String password;

    @Value("${debezium.postgres.dbname}")
    private String dbname;

    @Value("${debezium.postgres.server.name}")
    private String serverName;

    @Value("${debezium.postgres.plugin.name}")
    private String pluginName;

    @Value("${debezium.postgres.slot.name}")
    private String slotName;

    @Value("${debezium.postgres.table.include.list}")
    private String tableIncludeList;


    @Bean
    public DebeziumEngine<ChangeEvent<String, String>> debeziumEngineInit(
            Environment env, DebeziumChangeListener listener
    ) {
        io.debezium.config.Configuration config = io.debezium.config.Configuration.create()
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
                .with("offset.storage.file.filename", "C:/Users/PraveenRao/tarento/logs/offsets.dat")
                .with("offset.flush.interval.ms", "1000")
                .build();

        return DebeziumEngine.create(Json.class)
                .using(config.asProperties())
                .notifying(listener::handleChange)
                .build();
    }
}
