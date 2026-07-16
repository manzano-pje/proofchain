package com.proofchain.admin.institution.infrastructure.repository;

import com.proofchain.admin.institution.domain.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    Optional<Institution> findByCnpjAndDeletedAtIsNull(String cnpj);
    Optional<Institution> findByCnpj(String cnpj);

    List<Institution> findAllByDeletedAtIsNull();
    Optional<Institution> findByIdAndDeletedAtIsNull(Long Id);
    boolean existsByIdAndDeletedAtIsNull(Long Id);
}
