package com.proofchain.repository;

import com.proofchain.identities.Plans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlansRepository extends JpaRepository<Plans, Integer> {
}
