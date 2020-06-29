package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByUid(String uid);

    @Query(value = "SELECT payment.amount-sum(cancel.amount) as amount FROM payment inner join cancel where payment.uid = ?1", nativeQuery = true)
    Optional<Integer> getCurrentAmount(String uid);

    @Query(value = "SELECT payment.vat-sum(cancel.vat) as vat FROM payment inner join cancel where payment.uid = ?1", nativeQuery = true)
    Optional<Integer> getCurrentVat(String uid);
}
