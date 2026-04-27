package com.proofchain.instituition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstituitionRepository extends JpaRepository<Instituition, Long> {

//    Optional<Instituition> findByid(Long instituitionId);
    Optional<Instituition> findByCnpj(String cnpj);
    void deleteByCnpj(String cnpj);
}
