package com.proofchain.institution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

//    Optional<Instituition> findByid(Long instituitionId);
    Optional<Institution> findByCnpjAndDeletedAtIsNull(String cnpj);
    Optional<Institution> findByCnpj(String cnpj);

}
