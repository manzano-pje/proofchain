package com.proofchain.user.infrastructure.repository;

import com.proofchain.user.domain.model.User;
import com.proofchain.user.domain.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndInstitution_Id(String email, Long id);
    Optional<User> findByIdAndInstitution_Id(Long id, Long InstitutionId);
    Optional<User> findByEmail(String email);
    List<User> findAllByInstitution_IdAndInstitution_DeletedAtIsNull(Long institutionId);

    boolean existsByIdAndInstitutionId(Long id, Long institutionId);
    boolean existsByNameAndInstitutionId(String name, Long institutionId);
    boolean existsByEmail(String email);
    boolean existsByRole(UserRole userRole);

    }
