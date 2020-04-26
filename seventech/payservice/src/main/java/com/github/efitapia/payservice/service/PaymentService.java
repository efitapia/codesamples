package com.github.efitapia.payservice.service;

import com.github.efitapia.payservice.api.model.TopUpRequest;
import com.github.efitapia.payservice.api.model.TransferRequest;
import com.github.efitapia.payservice.api.model.WithDrawRequest;
import com.github.efitapia.payservice.repo.Account;
import com.github.efitapia.payservice.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final AccountRepository repo;

    public void transfer(TransferRequest request) {
        Account senderAccount;
        Account recipientAccount;

        // prevent deadlock
        if (request.getSenderAccountId() <= request.getRecipientAccountId()) {
            senderAccount = repo.lockById(request.getSenderAccountId());
            recipientAccount = repo.lockById(request.getRecipientAccountId());
        } else {
            recipientAccount = repo.lockById(request.getRecipientAccountId());
            senderAccount = repo.lockById(request.getSenderAccountId());
        }

        BigDecimal rest = senderAccount.getBalance().subtract(request.getAmount());
        if (rest.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughMoneyException(senderAccount.getBalance());
        }

        senderAccount.setBalance(rest);
        recipientAccount.setBalance(
            recipientAccount.getBalance().add(request.getAmount())
        );

        repo.save(senderAccount);
        repo.save(recipientAccount);
    }

    public void topUp(TopUpRequest request) {
        Account account = repo.lockById(request.getAccountId());

        account.setBalance(
            account.getBalance().add(request.getAmount())
        );

        repo.save(account);
    }

    public void withdraw(WithDrawRequest request) {
        Account account = repo.lockById(request.getAccountId());

        BigDecimal rest = account.getBalance().subtract(request.getAmount());
        if (rest.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughMoneyException(account.getBalance());
        }

        account.setBalance(rest);
        repo.save(account);
    }

}
