package com.providerService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.providerService.model.Provider;

public interface providerRepository extends JpaRepository<Provider,Long>{

    Provider findByUserName(String username);

}
