package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.domain.entity.Cancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancelRepository extends JpaRepository<Cancel, Long> {
    Optional<Cancel> findByUid(String uid);
}
