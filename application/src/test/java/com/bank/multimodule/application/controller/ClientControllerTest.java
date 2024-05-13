package com.bank.multimodule.application.controller;

import com.bank.multimodule.model.Client;
import com.bank.multimodule.model.Gender;
import com.bank.multimodule.model.commons.Result;
import com.bank.multimodule.service.ClientUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class ClientControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientUseCase clientUseCase;

    @Test
    void testGetByNit() throws Exception {
        Client client = new Client();
        client.setNit(1245L);
        client.setName("testName");
        client.setGender(Gender.MALE);
        client.setAddress("Test Address");
        client.setPhoneNumber(123456789L);
        client.setPassword("testPassword");
        client.setState(true);
        when(clientUseCase.createClient(refEq(client)))
                .thenReturn(new Result<>(client));

        mockMvc.perform(
                        post("/api/clientes/nuevo")
                                .content(objectMapper.writeValueAsString(client))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$..name", hasItem("testName")))
                .andExpect(jsonPath("$").isNotEmpty());

    }


    @Test
    void testUpdateClientInformation() throws Exception {
        Long nit = 1245L;
        Client client = new Client();
        client.setNit(nit);
        client.setName("testName");
        client.setGender(Gender.MALE);
        client.setAddress("Test Address");
        client.setPhoneNumber(123456789L);
        client.setPassword("testPassword");
        client.setState(true);
        when(clientUseCase.updateClientInformation(refEq(client)))
                .thenReturn(new Result<>(client));

        mockMvc.perform(
                        put("/api/clientes/{nit}", nit)
                                .content(objectMapper.writeValueAsString(client))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$..nit", hasItem(nit.intValue())))
                .andExpect(jsonPath("$..name", hasItem("testName")))
                .andExpect(jsonPath("$").isNotEmpty());
    }


    @Test
    void testDeactivateClient() throws Exception {
        Long nit = 1245L;
        Client client = new Client();
        client.setNit(nit);
        client.setName("testName");
        client.setGender(Gender.MALE);
        client.setAddress("Test Address");
        client.setPhoneNumber(123456789L);
        client.setPassword("testPassword");
        client.setState(false);
        when(clientUseCase.deactivateClient(eq(nit)))
                .thenReturn(new Result<>(client));

        mockMvc.perform(
                        put("/api/clientes/desactivar/{nit}", nit)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$..nit", hasItem(nit.intValue())))
                .andExpect(jsonPath("$..name", hasItem("testName")))
                .andExpect(jsonPath("$..state", hasItem(false)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testDeleteClientByNit() throws Exception {
        Long nit = 1245L;
        when(clientUseCase.deleteClientByNit(eq(nit)))
                .thenReturn(new Result<>(true));

        mockMvc.perform(
                        delete("/api/clientes/{nit}", nit)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.value", is(true)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

}