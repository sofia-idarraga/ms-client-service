package com.aditya.multimodule.application.controller;


import com.aditya.multimodule.model.Client;
import com.aditya.multimodule.model.commons.ErrorCode;
import com.aditya.multimodule.model.commons.Result;
import com.aditya.multimodule.service.ClientUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientController {

    private final ClientUseCase clientUseCase;

    public ClientController(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "I'm alive!";
    }

    @PostMapping("/nuevo")
    public ResponseEntity<Result<Client>> saveClient(@RequestBody Client client) {

        return processResult(clientUseCase.createClient(client));

    }

    @GetMapping("/{nit}")
    public ResponseEntity<Result<Client>> findClientByNit(@PathVariable Long nit) {
        Result<Client> result = clientUseCase.findClientByNit(nit);
        return processResult(result);
    }

    @GetMapping("/todos")
    public ResponseEntity<Result<List<Client>>> findAllClients(@RequestParam(defaultValue = "ASC") String direction) {
        String sort = "ASC";
        if (direction.equals("ASC") || direction.equals("DESC")) {
            sort = direction;
        }
        Result<List<Client>> result = clientUseCase.findAllClients(sort);
        return processResult(result);
    }

    @GetMapping("/todos/page")
    public ResponseEntity<Result<List<Client>>> findAllClientsPage(@RequestParam(defaultValue = "1") int pageNumber,
                                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                                   @RequestParam(defaultValue = "ASC") String direction) {
        String sort = "ASC";
        if (direction.equals("ASC") || direction.equals("DESC")) {
            sort = direction;
        }
        Result<List<Client>> result = clientUseCase.findAllClients(pageNumber, pageSize, sort);
        return processResult(result);
    }

    @PutMapping("/{nit}")
    public ResponseEntity<Result<Client>> updateClientInformation(@PathVariable Long nit, @RequestBody Client client) {
        client.setNit(nit);
        Result<Client> result = clientUseCase.updateClientInformation(client);
        return processResult(result);
    }

    @DeleteMapping("/{nit}")
    public ResponseEntity<Result<Boolean>> deleteClientByNit(@PathVariable Long nit) {
        Result<Boolean> result = clientUseCase.deleteClientByNit(nit);
        return processResult(result);
    }

    private <T> ResponseEntity<Result<T>> processResult(Result<T> result) {
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            ErrorCode errorCode = result.getErrors().get(0).getCode();
            return switch (errorCode) {
                case SERVER_ERROR -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
                case CLIENT_ERROR -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
                case GENERIC_ERROR -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
            };
        }
    }
}