package com.bank.multimodule.repository;

import com.bank.multimodule.model.Basic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicRepository extends JpaRepository<Basic, Integer>
{

}
