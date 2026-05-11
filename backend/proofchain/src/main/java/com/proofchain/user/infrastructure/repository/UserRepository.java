package com.proofchain.user.infrastructure.repository;

import com.proofchain.user.domain.model.User;
import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndInstitutionId(String email, Long id);
    Optional<User> findByEmail(String email);
    boolean existsByIdAndInstitutionId(Long id, Long institutionId);
    boolean existsByNameAndInstitutionId(String name, Long institutionId);
    boolean existsByEmail(String email);

    @Query("""
        SELECT u
        FROM User u
        WHERE u.institution.id = :institutionId
        AND u.institution.deletedAt IS NULL
    """)
    List<User> findActiveUsersByInstitution(Long institutionId);

}
