package com.aditya.multimodule.rest;

import com.aditya.multimodule.model.commons.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClientAdapterImpl implements RestClientAdapter{

    private final RestTemplate template;

    public RestClientAdapterImpl(RestTemplate template) {
        this.template = template;
    }

    @Override
    public Result<String> consume() {
        String url = "http://PSOFKA01149:8088/hello";
        ResponseEntity<String> responseEntity = template
                .getForEntity(url, String.class);
        return new Result<>(responseEntity.getBody());
    }
}
