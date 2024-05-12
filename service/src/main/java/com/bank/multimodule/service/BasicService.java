package com.bank.multimodule.service;

import com.bank.multimodule.model.Basic;

import java.util.List;
import java.util.Optional;

public interface BasicService
{
    Basic save(Basic theBasic);
    List<Basic> findAll();
    Optional<Basic> findById(int id);
    void deleteById(int id);
}
