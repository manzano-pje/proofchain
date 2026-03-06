package com.proofchain.repository;

import com.proofchain.identities.Plans;
import com.proofchain.identities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlansRepository extends JpaRepository<Plans, Integer> {

    Optional<Plans> findByName(String name);
}
