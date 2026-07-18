package com.proofchain.admin.subscription.infrastructure.repository;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.subscription.domain.model.Subscriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscriptions, Long> {

 void deleteByInstitution(Institution institution);
}
;