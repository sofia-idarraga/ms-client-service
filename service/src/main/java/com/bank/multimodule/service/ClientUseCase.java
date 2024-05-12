package com.bank.multimodule.service;

import com.bank.multimodule.model.Client;
import com.bank.multimodule.model.commons.Result;

import java.util.List;

public interface ClientUseCase {

    Result<Client> createClient(Client client);
    Result<Client> findClientByNit(Long nit);
    Result<List<Client>> findAllClients(String direction);
    Result<List<Client>> findAllClients(int pageNumber, int pageSize, String sortDirection);
    Result<Client> updateClientInformation(Client client);

    Result<Client> deactivateClient(Long clientId);
    Result<Boolean> deleteClientByNit(Long nit);
}