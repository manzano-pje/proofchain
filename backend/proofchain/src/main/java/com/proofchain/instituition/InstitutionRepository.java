package com.proofchain.instituition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

//    Optional<Institution> findByid(Long instituitionId);
    Optional<Institution> findByCnpj(String cnpj);
    void deleteByCnpj(String cnpj);
}
