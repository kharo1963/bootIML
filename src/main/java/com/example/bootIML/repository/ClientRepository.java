package com.example.bootIML.repository;

import com.example.bootIML.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer>  {
}
