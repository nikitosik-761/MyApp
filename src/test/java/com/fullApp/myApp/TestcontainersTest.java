package com.fullApp.myApp;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TestcontainersTest extends AbstractTestcontainers {


    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();

    }

    //docker exec -it CONTAINER_NAME bash
    //psql -U username -d dao-unit-test


}
