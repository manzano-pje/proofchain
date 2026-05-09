package com.proofchain.subscription;

import com.proofchain.institution.domain.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscriptions, Long> {

 void deleteByInstitution(Institution institution);
}
;