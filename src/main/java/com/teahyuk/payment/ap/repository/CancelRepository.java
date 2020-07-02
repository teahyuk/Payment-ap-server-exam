package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.domain.entity.CancelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.Optional;

@Repository
public interface CancelRepository extends JpaRepository<CancelEntity, Long> {
    Optional<CancelEntity> findByUid(String uid);

    @Query(value = "SELECT sum(cancel.amount) as amount, sum(cancel.vat) as vat FROM " +
            "payment " +
            "inner join cancel where payment.uid = ?1",
            nativeQuery = true)
    Optional<Tuple> getCanceledPrices(String uid);
}
