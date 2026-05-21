package com.proofchain.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlansRepository extends JpaRepository<Plans, Long> {

    Optional<Plans> findByName(String name);
}
