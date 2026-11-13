package com.proofchain.user.infrastructure.repository;

import com.proofchain.user.domain.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    void deleteByUser_id(Long id);
    Optional<PasswordResetToken>findByUser_Id(Long id);
}
