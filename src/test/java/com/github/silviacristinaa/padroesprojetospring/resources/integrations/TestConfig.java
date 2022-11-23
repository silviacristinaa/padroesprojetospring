package com.github.silviacristinaa.padroesprojetospring.resources.integrations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.github.tomakehurst.wiremock.client.WireMock;

@TestConfiguration
public class TestConfig {
	
    @Bean
    public WireMock wireMock(@Value("${wiremock.server.port}") Integer port) {
        return WireMock.create()
                .port(port)
                .build();
    }
}
