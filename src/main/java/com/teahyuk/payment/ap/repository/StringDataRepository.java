package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.entity.Payment;
import com.teahyuk.payment.ap.entity.StringData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StringDataRepository extends JpaRepository<StringData, Long> {
    Optional<StringData> findByUid(String uid);
}
