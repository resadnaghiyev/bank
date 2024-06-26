package com.rashad.bank.api.repository;

import com.rashad.bank.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPin(String pin);

    Optional<User> findByPin(String pin);

}
