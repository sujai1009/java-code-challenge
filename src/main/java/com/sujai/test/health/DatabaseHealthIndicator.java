package com.sujai.test.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            // The datasource connectivity is implicitly checked by Spring Boot's
            // DataSourceHealthIndicator. This custom indicator adds additional
            // context about the database being used for device persistence.
            return Health.up()
                    .withDetail("database", "MySQL")
                    .withDetail("purpose", "Device persistence")
                    .build();
        } catch (Exception ex) {
            return Health.down(ex).build();
        }
    }
}
