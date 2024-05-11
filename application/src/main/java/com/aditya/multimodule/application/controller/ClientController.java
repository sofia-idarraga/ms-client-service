package com.aditya.multimodule.application.controller;


import com.aditya.multimodule.model.Client;
import com.aditya.multimodule.model.commons.Result;
import com.aditya.multimodule.service.ClientUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.aditya.multimodule.model.commons.ErrorCode.GENERIC_ERROR;

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

    private ResponseEntity processResult(Result result) {
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else if (result.validateErrorCode(GENERIC_ERROR)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
        }
    }
}