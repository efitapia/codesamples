package com.github.efitapia.payservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

interface AccountJpaRepository extends JpaRepository<Account, Long> {

    @Lock(PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account> lockById(Long id);

}
