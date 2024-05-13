package com.bank.multimodule.service.impl;

import com.bank.multimodule.model.Client;
import com.bank.multimodule.model.Gender;
import com.bank.multimodule.model.commons.ErrorCode;
import com.bank.multimodule.model.commons.Result;
import com.bank.multimodule.model.commons.ResultError;
import com.bank.multimodule.repository.adapters.ClientAdapter;
import com.bank.multimodule.rest.RestClientAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ClientUseCaseImplTest {

    @Mock
    private ClientAdapter clientAdapter;
    @Mock
    private RestClientAdapter restClientAdapter;

    @InjectMocks
    private ClientUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateClient_Success() {
        Client client = testClient();

        when(clientAdapter.save(client)).thenReturn(new Result<>(client));

        Result<Client> result = useCase.createClient(client);

        assertTrue(result.isSuccess());
        assertNotNull(result);
        assertEquals(client, result.getValue());
        assertEquals(client.getPassword(), result.getValue().getPassword());

        verify(clientAdapter, times(1)).save(client);
        verify(restClientAdapter, times(0)).deactivate(anyString());

    }

    @Test
    void testCreateClient_Failure() {
        Client client = new Client();
        Result<Client> expectedResult = new Result<>();
        expectedResult.addError(new ResultError(ErrorCode.SERVER_ERROR, "Error saving client"));

        when(clientAdapter.save(client)).thenReturn(expectedResult);

        Result<Client> result = useCase.createClient(client);

        assertFalse(result.isSuccess());
        assertFalse(result.getErrors().isEmpty());
        assertEquals("Error saving client", result.getErrors().get(0).getMessage());
        verify(clientAdapter, times(1)).save(client);
        verify(restClientAdapter, times(0)).deactivate(anyString());
    }

    @Test
    void testFindClientByNit_Success() {
        Long nit = 12345L;
        Client client = testClient();
        when(clientAdapter.findById(nit)).thenReturn(new Result<>(client));
        Result<Client> result = useCase.findClientByNit(nit);

        assertTrue(result.isSuccess());
        assertNotNull(result);
        assertEquals(client, result.getValue());
        assertEquals(client.getName(), result.getValue().getName());

        verify(clientAdapter, times(1)).findById(nit);
        verify(restClientAdapter, times(0)).deactivate(anyString());
    }

    @Test
    void testFindClientByNit_NotFound() {
        Long nit = 54321L;

        Result<Client> expectedResult = new Result<>();
        expectedResult.addError(new ResultError(ErrorCode.NOT_FOUND, "Client not found"));

        when(clientAdapter.findById(nit)).thenReturn(expectedResult);

        Result<Client> result = useCase.findClientByNit(nit);
        assertFalse(result.isSuccess());
        assertFalse(result.getErrors().isEmpty());
        assertEquals("Client not found", result.getErrors().get(0).getMessage());

        verify(clientAdapter, times(1)).findById(nit);
        verify(restClientAdapter, times(0)).deactivate(anyString());
    }

    @Test
    void testFindAllClients_Success() {
        String direction = "ASC";
        List<Client> expectedClients = List.of(testClient());
        when(clientAdapter.findAll(direction)).thenReturn(new Result<>(expectedClients));
        Result<List<Client>> result = useCase.findAllClients(direction);

        assertNotNull(result);
        assertEquals(expectedClients, result.getValue());

        verify(clientAdapter, times(1)).findAll(direction);
        verify(restClientAdapter, times(0)).deactivate(anyString());
    }

    @Test
    void testFindAllClients_Failure() {
        String direction = "DESC";
        Result<List<Client>> expectedResult = new Result<>();
        expectedResult.addError(new ResultError(ErrorCode.SERVER_ERROR, "Error getting clients"));
        when(clientAdapter.findAll(direction)).thenReturn(expectedResult);
        Result<List<Client>> result = useCase.findAllClients(direction);
        assertNotNull(result);
        assertEquals(expectedResult, result);

        verify(clientAdapter, times(1)).findAll(direction);
        verify(restClientAdapter, times(0)).deactivate(anyString());
    }

    @Test
    void testDeactivateClient_Success() {
        Long nit = 12345L;
        Result<Boolean> deleteAccountsResult = new Result<>(true);
        when(restClientAdapter.deactivate(String.valueOf(nit))).thenReturn(deleteAccountsResult);
        Client client = testClient();
        client.setState(false);

        Result<Client> expectedResult = new Result<>(client);
        when(clientAdapter.deactivateClient(nit)).thenReturn(expectedResult);

        Result<Client> result = useCase.deactivateClient(nit);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(expectedResult, result);
        assertFalse(result.getValue().getState());

        verify(clientAdapter, times(1)).deactivateClient(nit);
        verify(restClientAdapter, times(1)).deactivate(String.valueOf(nit));
    }

    @Test
    void testDeactivateClient_Failure() {
        Long nit = 12345L;
        Result<Boolean> deleteAccountsResult = new Result<>();
        deleteAccountsResult.addError(new ResultError(ErrorCode.SERVER_ERROR, "Error al desactivar el cliente"));

        when(restClientAdapter.deactivate(String.valueOf(nit))).thenReturn(deleteAccountsResult);

        Result<Client> result = useCase.deactivateClient(nit);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error al desactivar el cliente", result.getErrors().get(0).getMessage());

        verify(clientAdapter, times(0)).deactivateClient(nit);
        verify(restClientAdapter, times(1)).deactivate(String.valueOf(nit));
    }

    @Test
    void testDeleteClientByNit_Success() {
        Long nit = 12345L;
        Result<Boolean> deleteAccountsResult = new Result<>(true);

        when(restClientAdapter.deactivate(String.valueOf(nit)))
                .thenReturn(deleteAccountsResult);

        Result<Boolean> expectedResult = new Result<>(true);
        when(clientAdapter.deleteById(nit)).thenReturn(expectedResult);

        Result<Boolean> result = useCase.deleteClientByNit(nit);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(expectedResult, result);

        verify(clientAdapter, times(1)).deleteById(nit);
        verify(restClientAdapter, times(1)).deactivate(String.valueOf(nit));
    }

    @Test
    void testDeleteClientByNit_Failure() {
        Long nit = 12345L;
        Result<Boolean> deleteAccountsResult = new Result<>();
        deleteAccountsResult.addError(new ResultError(ErrorCode.SERVER_ERROR, "Error deleting client"));
        when(restClientAdapter.deactivate(String.valueOf(nit))).thenReturn(deleteAccountsResult);

        Result<Boolean> result = useCase.deleteClientByNit(nit);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(deleteAccountsResult.getErrors().get(0), result.getErrors().get(0));

        verify(clientAdapter, times(0)).deactivateClient(nit);
        verify(restClientAdapter, times(1)).deactivate(String.valueOf(nit));
    }

    private static Client testClient() {
        Client client = new Client();
        client.setNit(1245L);
        client.setName("testName");
        client.setGender(Gender.MALE);
        client.setAddress("Test Address");
        client.setPhoneNumber(123456789L);
        client.setPassword("testPassword");
        client.setState(true);
        return client;
    }

}