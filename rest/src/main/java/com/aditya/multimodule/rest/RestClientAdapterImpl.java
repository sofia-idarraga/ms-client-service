package com.aditya.multimodule.rest;

import com.aditya.multimodule.model.commons.ErrorCode;
import com.aditya.multimodule.model.commons.Result;
import com.aditya.multimodule.model.commons.ResultError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClientAdapterImpl implements RestClientAdapter {
    private final String baseUrl;
    private final RestTemplate template;

    public RestClientAdapterImpl(@Value("${spring.account.service}") String baseUrl, RestTemplate template) {
        this.baseUrl = baseUrl;
        this.template = template;
    }

    @Override
    public Result<Boolean> delete(String nit) {
        String url = baseUrl.concat("/");
//                .concat(nit);
        try {
            ResponseEntity<Void> responseEntity = template
                    .exchange(url, HttpMethod.DELETE, null, Void.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return new Result<>(true);
            } else {

                return new Result<Boolean>().addError(new ResultError(ErrorCode.SERVER_ERROR,
                        "Error trying to delete the client. Status code: " + responseEntity.getStatusCodeValue()));
            }
        } catch (RestClientException e) {
            // Maneja excepciones de cliente REST aquí
            return new Result<Boolean>().addError(new ResultError(ErrorCode.CLIENT_ERROR,
                    "Error communicating with the server: " + e.getMessage()));
        }
    }
}
