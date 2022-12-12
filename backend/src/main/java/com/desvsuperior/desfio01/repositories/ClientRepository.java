package com.desvsuperior.desfio01.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desvsuperior.desfio01.entities.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
