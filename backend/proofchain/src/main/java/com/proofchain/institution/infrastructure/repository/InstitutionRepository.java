package com.proofchain.institution.infrastructure.repository;

import com.proofchain.institution.domain.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    Optional<Institution> findByCnpjAndDeletedAtIsNull(String cnpj);
    Optional<Institution> findByCnpj(String cnpj);
    Optional<Institution> findByIdAndDeletedAtIsNull(Long id);
    List<Institution> findAllByDeletedAtIsNull();
    boolean existsByIdAndDeletedAtIsNull(Long Id);
    boolean existsById(Long institutionId);

}
