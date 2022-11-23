package com.github.silviacristinaa.padroesprojetospring.resources.integrations;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestConfig.class})
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = Options.DYNAMIC_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTests {

	@Autowired
	protected MockMvc mvc;
	@Autowired
	protected WireMock wireMock;

	@Autowired
	protected ObjectMapper objectMapper;

	protected static HttpHeaders mockHttpHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");
		return httpHeaders;
	}
	
	protected static String getIdByLocation(String location) {
	    assertNotNull(location);
	    return location.substring(location.lastIndexOf("/") + 1);
	  }

	@BeforeAll
	void basicSetup() {
		wireMock.register(get("https://viacep.com.br/ws/**")
				.willReturn(ok().withHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.withHeader(org.apache.http.HttpHeaders.CONNECTION, "close")
						.withBodyFile("viacep-return.json")));
		wireMock.register(get("https://viacep.com.br/ws/0/json/")
				.willReturn(ok().withHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.withHeader(org.apache.http.HttpHeaders.CONNECTION, "close")
						.withBodyFile("viacep-return-error.json")));
	}

	@AfterAll
	void clean() {
		wireMock.resetMappings();
		wireMock.resetRequests();
	}
}
