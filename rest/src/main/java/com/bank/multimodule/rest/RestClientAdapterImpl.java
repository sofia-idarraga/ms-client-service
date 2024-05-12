package com.bank.multimodule.rest;

import com.bank.multimodule.model.commons.ErrorCode;
import com.bank.multimodule.model.commons.Result;
import com.bank.multimodule.model.commons.ResultError;
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
    public Result<Boolean> deactivate(String nit) {
        String url = baseUrl.concat("/desactivar/").concat(nit);

        try {
            ResponseEntity<Void> responseEntity = template
                    .exchange(url, HttpMethod.PUT, null, Void.class);

            return validateResult(responseEntity, "Error trying to deactivate the client's account. Status code: ");
        } catch (RestClientException e) {
            return new Result<Boolean>().addError(new ResultError(ErrorCode.CLIENT_ERROR,
                    "Error communicating with the server: " + e.getMessage()));
        }
    }


    private static Result<Boolean> validateResult(ResponseEntity<Void> responseEntity, String x) {
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return new Result<>(true);
        } else {

            return new Result<Boolean>().addError(new ResultError(ErrorCode.SERVER_ERROR,
                    x + responseEntity.getStatusCodeValue()));
        }
    }
}
