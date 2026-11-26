package com.proofchain.auth.repository;

import com.proofchain.auth.Entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    void deleteByUser_Id(Long id);
    Optional<PasswordResetToken>findByUser_Id(Long id);
}
