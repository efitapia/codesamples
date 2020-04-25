package com.github.efitapia.payservice.api.controller;

import com.github.efitapia.payservice.api.model.TopUpRequest;
import com.github.efitapia.payservice.api.model.TransferRequest;
import com.github.efitapia.payservice.api.model.WithDrawRequest;
import com.github.efitapia.payservice.repo.Account;
import com.github.efitapia.payservice.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final AccountRepository repo;

    @Transactional
    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequest request) {
        Account senderAccount = repo.lockById(request.getSenderAccountId())
            .orElseThrow(() -> new IllegalArgumentException("No such account " + request.getSenderAccountId()));

        Account recipientAccount = repo.lockById(request.getRecipientAccountId())
            .orElseThrow(() -> new IllegalArgumentException("No such account " + request.getRecipientAccountId()));

        BigDecimal rest = senderAccount.getBalance().subtract(request.getAmount());
        if (rest.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Not enough money. Balance: " + senderAccount.getBalance());
        }

        senderAccount.setBalance(rest);
        recipientAccount.setBalance(
            recipientAccount.getBalance().add(request.getAmount())
        );

        repo.save(senderAccount);
        repo.save(recipientAccount);
    }


    @Transactional
    @PostMapping("/topUp")
    public void topUp(TopUpRequest request) {
        Account account = repo.lockById(request.getAccountId())
            .orElseThrow(() -> new IllegalArgumentException("No such account " + request));

        account.setBalance(
            account.getBalance().add(request.getAmount())
        );

        repo.save(account);
    }


    @Transactional
    @PostMapping("/withdraw")
    public void withdraw(WithDrawRequest request) {
        Account account = repo.lockById(request.getAccountId())
            .orElseThrow(() -> new IllegalArgumentException("No such account " + request));

        BigDecimal rest = account.getBalance().subtract(request.getAmount());
        if (rest.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Not enough money. Balance: " + account.getBalance());
        }

        account.setBalance(rest);
        repo.save(account);
    }

}
