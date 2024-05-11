package com.aditya.multimodule.service.impl;

import com.aditya.multimodule.model.Client;
import com.aditya.multimodule.model.commons.Result;
import com.aditya.multimodule.repository.adapters.ClientAdapter;
import com.aditya.multimodule.rest.RestClientAdapter;
import com.aditya.multimodule.service.ClientUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class ClientUseCaseImpl implements ClientUseCase {

    private final ClientAdapter clientAdapter;
    private final RestClientAdapter restClientAdapter;


    public ClientUseCaseImpl(ClientAdapter clientAdapter, RestClientAdapter restClientAdapter) {
        this.clientAdapter = clientAdapter;
        this.restClientAdapter = restClientAdapter;
    }

    @Override
    public Result<Client> createClient(Client client) {
        return clientAdapter.save(client);
    }

    public Result<String> callClient(){
        return restClientAdapter.consume();
    }
    @Override
    public Result<Client> findClientByNit(Long nit) {
        return clientAdapter.findById(nit);
    }

    @Override
    public Result<List<Client>> findAllClients(String direction, String property) {
        return clientAdapter.findAll(direction, property);
    }

    @Override
    public Result<List<Client>> findAllClients(int pageNumber, int pageSize, String sortDirection, String property) {
        return clientAdapter.findAll(pageNumber, pageSize, sortDirection, property);
    }

    @Override
    public Result<Client> updateClientInformation(Client client) {
        return clientAdapter.update(client);
    }

    @Override
    public Result<Boolean> deleteClientByNit(Long nit) {
        return clientAdapter.deleteById(nit);
    }
}
