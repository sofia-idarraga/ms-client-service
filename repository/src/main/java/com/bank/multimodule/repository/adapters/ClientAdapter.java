package com.bank.multimodule.repository.adapters;


import com.bank.multimodule.model.Client;
import com.bank.multimodule.model.commons.Result;

public interface ClientAdapter extends BaseAdapter<Client, Long> {

    Result<Client> deactivateClient(Long clientId);
}
