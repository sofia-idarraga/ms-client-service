package com.aditya.multimodule.repository.adapters;


import com.aditya.multimodule.model.Client;
import com.aditya.multimodule.model.Gender;
import com.aditya.multimodule.model.commons.Result;
import com.aditya.multimodule.repository.enitites.ClientEntity;
import com.aditya.multimodule.repository.repositories.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientAdapterImpl implements ClientAdapter {

    private final ClientRepository repository;
    private final ModelMapper modelMapper;

    public ClientAdapterImpl(ClientRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Result<Client> save(Client model) {
        return new Result<>(entityToModel(repository.save(modelToEntity(model))));

    }

    @Override
    public Result<Client> findById(Long aLong) {
        return null;
    }

    @Override
    public Result<Client> update(Client id) {
        return null;
    }

    @Override
    public Result<List<Client>> findAll(String direction, String property) {
        return null;
    }

    @Override
    public Result<List<Client>> findAll(int pageNumber, int pageSize, String direction, String property) {
        return null;
    }

    @Override
    public Result<Boolean> deleteById(Long aLong) {
        return null;
    }

    private ClientEntity modelToEntity(Client model) {
        return modelMapper.map(model, ClientEntity.class);
    }

    private Client entityToModel(ClientEntity entity) {
        return modelMapper.map(entity, Client.class);
    }
}
