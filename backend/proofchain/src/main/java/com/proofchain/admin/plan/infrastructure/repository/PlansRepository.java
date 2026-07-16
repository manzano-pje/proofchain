package com.proofchain.admin.plan.infrastructure.repository;

import com.proofchain.admin.plan.domain.model.Plans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlansRepository extends JpaRepository<Plans, Long> {

    Optional<Plans> findByName(String name);

}
