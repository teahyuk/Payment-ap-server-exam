package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.domain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByUid(String uid);

    @Query(value = "SELECT payment.amount-sum(cancel.amount) as amount, payment.vat-sum(cancel.vat) as vat FROM " +
            "payment " +
            "inner join cancel where payment.uid = ?1",
            nativeQuery = true)
    Optional<Tuple> getCurrentPrice(String uid);
}
