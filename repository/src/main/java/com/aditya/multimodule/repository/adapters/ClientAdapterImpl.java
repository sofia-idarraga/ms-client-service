package com.aditya.multimodule.repository.adapters;


import com.aditya.multimodule.model.Client;
import com.aditya.multimodule.model.commons.ErrorCode;
import com.aditya.multimodule.model.commons.Result;
import com.aditya.multimodule.model.commons.ResultError;
import com.aditya.multimodule.repository.enitites.ClientEntity;
import com.aditya.multimodule.repository.repositories.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        try {
            return new Result<>(entityToModel(repository.save(modelToEntity(model))));
        } catch (Exception exception) {
            return new Result<Client>().addError(new ResultError(ErrorCode.SERVER_ERROR,
                    exception.getMessage()));
        }


    }

    @Override
    public Result<Client> findById(Long aLong) {
        try {
            Optional<ClientEntity> result = repository.findById(aLong);
            if (result.isPresent()) {
                return new Result<>(
                        entityToModel(result.get()));
            } else {
                return new Result<Client>().addError(new ResultError(ErrorCode.NOT_FOUND,
                        "Item not found"));
            }
        } catch (Exception exception) {
            return new Result<Client>().addError(new ResultError(ErrorCode.SERVER_ERROR,
                    exception.getMessage()));
        }
    }

    @Override
    public Result<Client> update(Client client) {
        try {
            Optional<ClientEntity> optionalClient = repository.findById(client.getNit());

            if (optionalClient.isPresent()) {

                ClientEntity existingClient = optionalClient.get();


                existingClient.setName(client.getName());
                existingClient.setGender(client.getGender().toString());
                existingClient.setAddress(client.getAddress());
                existingClient.setPhoneNumber(client.getPhoneNumber());
                existingClient.setPassword(client.getPassword());
                existingClient.setState(client.getState());

                repository.save(existingClient);

                return new Result<>(entityToModel(existingClient));
            } else {
                return new Result<Client>().addError(new ResultError(ErrorCode.NOT_FOUND,
                        "Client with NIT " + client.getNit() + " not found."));
            }
        } catch (Exception exception) {

            return new Result<Client>().addError(new ResultError(ErrorCode.SERVER_ERROR,
                    exception.getMessage()));
        }
    }

    @Override
    public Result<List<Client>> findAll(String direction) {
        try {
            Sort sort = Sort.by(direction.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "nit");
            return new Result<>(
                    repository.findAll(sort).stream()
                            .map(this::entityToModel).collect(Collectors.toList())
            );
        } catch (Exception exception) {
            return new Result<List<Client>>().addError(new ResultError(ErrorCode.SERVER_ERROR,
                    exception.getMessage()));
        }
    }

    @Override
    public Result<List<Client>> findAll(int pageNumber, int pageSize, String direction) {
        try {
            if (pageNumber <= 0 || pageSize <= 0) {
                return new Result<>((List.of()));
            }
            if (pageNumber == 1) {
                pageNumber = 0;
            }
            Sort sort = Sort.by(direction.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "nit");
            return new Result<>(
                    repository.findAll(sort)
                            .stream()
                            .skip((long) pageNumber * (long) pageSize)
                            .limit(pageSize)
                            .map(this::entityToModel)
                            .collect(Collectors.toList()));
        } catch (Exception exception) {
            return new Result<List<Client>>().addError(new ResultError(ErrorCode.SERVER_ERROR,
                    exception.getMessage()));
        }
    }

    @Override
    public Result<Boolean> deleteById(Long aLong) {
        try {
            repository.deleteById(aLong);
            return new Result<>(true);
        } catch (Exception exception) {
            return new Result<>(false);
        }
    }

    private ClientEntity modelToEntity(Client model) {
        return modelMapper.map(model, ClientEntity.class);
    }

    private Client entityToModel(ClientEntity entity) {
        return modelMapper.map(entity, Client.class);
    }
}
