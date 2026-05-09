package com.proofchain.user.infrastructure.repository;

import com.proofchain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByNameAndInstitutionId(String email, Long Institution);
    void deleteByEmail(String email);

}
