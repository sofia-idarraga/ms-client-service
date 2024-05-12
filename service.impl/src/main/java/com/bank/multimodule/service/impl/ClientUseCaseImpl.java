package com.bank.multimodule.service.impl;

import com.bank.multimodule.model.Client;
import com.bank.multimodule.model.commons.Result;
import com.bank.multimodule.repository.adapters.ClientAdapter;
import com.bank.multimodule.rest.RestClientAdapter;
import com.bank.multimodule.service.ClientUseCase;
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

    @Override
    public Result<Client> findClientByNit(Long nit) {
        return clientAdapter.findById(nit);
    }

    @Override
    public Result<List<Client>> findAllClients(String direction) {
        return clientAdapter.findAll(direction);
    }

    @Override
    public Result<List<Client>> findAllClients(int pageNumber, int pageSize, String sortDirection) {
        return clientAdapter.findAll(pageNumber, pageSize, sortDirection);
    }

    @Override
    public Result<Client> updateClientInformation(Client client) {
        return clientAdapter.update(client);
    }

    @Override
    public Result<Client> deactivateClient(Long clientId) {
        Result<Boolean> deleteAccountsResult = restClientAdapter.deactivate(String.valueOf(clientId));

        if (deleteAccountsResult.isSuccess()) {
            return clientAdapter.deactivateClient(clientId);

        } else {
            return new Result<Client>().addError(deleteAccountsResult.getErrors().get(0));
        }
    }

    @Override
    public Result<Boolean> deleteClientByNit(Long nit) {
        Result<Boolean> deleteAccountsResult = restClientAdapter.deactivate(String.valueOf(nit));
        if (deleteAccountsResult.isSuccess()) {
            return clientAdapter.deleteById(nit);

        } else {
            return new Result<Boolean>().addError(deleteAccountsResult.getErrors().get(0));
        }
    }
}
