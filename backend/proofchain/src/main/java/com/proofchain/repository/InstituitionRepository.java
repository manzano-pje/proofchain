package com.proofchain.repository;

import com.proofchain.identities.Instituition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstituitionRepository extends JpaRepository<Instituition, Long> {

    Optional<Instituition> findByidInstituition(Long institutionId);
    Optional<Instituition> findByCnpj(String cnpj);
    void deleteByCnpj(String cnpj);
}
