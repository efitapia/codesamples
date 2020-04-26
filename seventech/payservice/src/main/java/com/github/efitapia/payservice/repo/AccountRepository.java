package com.github.efitapia.payservice.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final AccountJpaRepository jpaRepo;

    public Account lockById(Long id) {
        return jpaRepo.lockById(id)
            .orElseThrow(() -> new NoSuchEntityException("No such account " + id));
    }

    public Account save(Account account) {
        return jpaRepo.save(account);
    }

    public Account get(Long id) {
        return jpaRepo.findById(id)
            .orElseThrow(() -> new NoSuchEntityException("No such account " + id));
    }

}
