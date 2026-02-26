package com.proofchain.repository;

import com.proofchain.identities.Subscriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscriptions, Long> {
}
