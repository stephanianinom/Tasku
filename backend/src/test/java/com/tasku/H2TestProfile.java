package com.tasku;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class H2TestProfile implements QuarkusTestProfile {
    
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
            "quarkus.datasource.db-kind", "h2",
            "quarkus.datasource.jdbc.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
            "quarkus.datasource.username", "sa",
            "quarkus.datasource.password", "sa",
            "quarkus.hibernate-orm.database.generation", "drop-and-create",
            "quarkus.hibernate-orm.log.sql", "false"
        );
    }
}

