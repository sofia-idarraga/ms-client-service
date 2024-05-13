package com.bank.multimodule.application.controller;

import com.bank.multimodule.model.Client;
import com.bank.multimodule.model.Gender;
import com.bank.multimodule.model.commons.Result;
import com.bank.multimodule.repository.adapters.ClientAdapter;
import com.bank.multimodule.repository.enitites.ClientEntity;
import com.bank.multimodule.repository.repositories.ClientRepository;
import com.bank.multimodule.service.ClientUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClientRepository repository;

    @Autowired
    private ClientAdapter adapter;

    @Autowired
    private ClientUseCase useCase;

    @Autowired
    private ModelMapper modelMapper;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.account.service", wireMockServer::baseUrl);
    }

    private static HttpHeaders headers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void getAll() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Result<List<Client>>> response = restTemplate.exchange(
                createURLWithPort() + "/todos", HttpMethod.GET, entity, new ParameterizedTypeReference<Result<List<Client>>>() {
                });
        Result<List<Client>> result = response.getBody();

        assertEquals(response.getStatusCodeValue(), 200);
        assertTrue(result.isSuccess());
        assertEquals(result.getValue().size(), repository.findAll().size());
        assertEquals(result.getValue().size(), useCase.findAllClients("DESC").getValue().size());
        assertEquals(result.getValue().size(), adapter.findAll("DESC").getValue().size());

    }

    @Test
    @Sql(statements = "INSERT INTO client(nit, name, gender, address, phone_number, password, state) VALUES (999, 'Juan', 'MALE', 'Calle Principal 123', 1234567890, 'password123', true)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM client WHERE nit='999'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testClientById() throws JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Result<Client>> response = restTemplate.exchange(
                createURLWithPort() + "/999", HttpMethod.GET, entity, new ParameterizedTypeReference<Result<Client>>() {
                });
        Result<Client> result = response.getBody();

        String expected = "{\"nit\":999,\"name\":\"Juan\",\"gender\":\"MALE\",\"address\":\"Calle Principal 123\",\"phoneNumber\":1234567890,\"password\":\"password123\",\"state\":true}";

        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals(expected, objectMapper.writeValueAsString(result.getValue()));
        assertTrue(result.isSuccess());

        assertEquals(result.getValue().getName(), useCase.findClientByNit(999L).getValue().getName());
        assertEquals(result.getValue().getPassword(), useCase.findClientByNit(999L).getValue().getPassword());
        assertEquals(result.getValue().getNit(), repository.findById(999L).orElse(null).getNit());
        assertEquals(result.getValue().getPhoneNumber(), adapter.findById(999L).getValue().getPhoneNumber());
    }

    @Test
    @Sql(statements = "DELETE FROM client WHERE nit='888'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testCreateClient() throws JsonProcessingException {
        Client client = new Client();
        client.setNit(1245L);
        client.setName("testName");
        client.setGender(Gender.MALE);
        client.setAddress("Test Address");
        client.setPhoneNumber(123456789L);
        client.setPassword("testPassword");
        client.setState(true);

        ClientEntity clientEntity = modelMapper.map(client, ClientEntity.class);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(client), headers);

        ResponseEntity<Result<Client>> response = restTemplate.exchange(
                createURLWithPort() + "/nuevo", HttpMethod.POST, entity, new ParameterizedTypeReference<Result<Client>>() {
                });
        assertEquals(response.getStatusCodeValue(), 200);

        Client clientRes = Objects.requireNonNull(response.getBody().getValue());
        assertEquals(clientRes.getName(), "testName");
        assertEquals(clientRes.getState(), repository.save(clientEntity).getState());
    }

    @Test
    @Sql(statements = "INSERT INTO client(nit, name, gender, address, phone_number, password, state) VALUES (777, 'Juan', 'MALE', 'Calle Principal 123', 1234567890, 'password123', true)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM client WHERE nit='777'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testDeleteClient() {
        wireMockServer.stubFor(WireMock.put(urlMatching("/desactivar/.*"))
                .willReturn(aResponse().withStatus(200)));

        ResponseEntity<Result<Boolean>> response = restTemplate.exchange(
                (createURLWithPort() + "/777"), HttpMethod.DELETE, null, new ParameterizedTypeReference<Result<Boolean>>() {
                });

        Boolean clientRes = response.getBody().getValue();
        assertEquals(response.getStatusCodeValue(), 200);
        assertNotNull(clientRes);
        assertTrue(response.getBody().isSuccess());
    }

    private String createURLWithPort() {
        return "http://localhost:" + port + "/api/clientes";
    }
}
