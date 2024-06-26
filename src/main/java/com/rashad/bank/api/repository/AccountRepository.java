package com.rashad.bank.api.repository;

import com.rashad.bank.api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNumber(String accountNumber);

    Optional<Account> findByNumberAndUserIdAndActiveIsTrue(String number, Long userId);

    List<Account> findByUserIdAndActiveIsTrue(Long userId);

    Optional<Account> findByNumber(String toAccountNumber);
}
