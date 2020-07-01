package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.domain.entity.PaymentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentStateRepository extends JpaRepository<PaymentState, Long> {
    Optional<PaymentState> findByUid(String uid);
}
