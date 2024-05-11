package com.aditya.multimodule.service;

import com.aditya.multimodule.model.Client;
import com.aditya.multimodule.model.commons.Result;

import java.util.List;

public interface ClientUseCase {

    Result<Client> createClient(Client client);
    Result<Client> findClientByNit(Long nit);
    Result<List<Client>> findAllClients(String direction);
    Result<List<Client>> findAllClients(int pageNumber, int pageSize, String sortDirection);
    Result<Client> updateClientInformation(Client client);
    Result<Boolean> deleteClientByNit(Long nit);
}