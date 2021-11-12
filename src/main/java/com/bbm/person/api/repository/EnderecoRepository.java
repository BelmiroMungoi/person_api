package com.bbm.person.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bbm.person.api.model.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long>{

}
