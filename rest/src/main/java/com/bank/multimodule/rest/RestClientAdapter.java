package com.bank.multimodule.rest;

import com.bank.multimodule.model.commons.Result;

public interface RestClientAdapter {
    Result<Boolean> deactivate(String nit);
}
