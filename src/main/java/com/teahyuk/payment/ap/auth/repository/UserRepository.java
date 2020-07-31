package com.teahyuk.payment.ap.auth.repository;

import com.teahyuk.payment.ap.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUid(String email);
}
