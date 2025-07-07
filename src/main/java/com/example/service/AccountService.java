package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.exception.ExistingUserException;
import com.example.exception.InvalidLoginException;
import com.example.exception.ValidationException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account registerAccount(Account account) throws ValidationException, ExistingUserException {
        if (account.getUsername().length() == 0)
            throw new ValidationException("Username must not be blank.");
        if (account.getPassword().length() < 4)
            throw new ValidationException("Password must be at least 4 characters.");
        if (this.accountRepository.existsByUsername(account.getUsername()))
            throw new ExistingUserException();
        return this.accountRepository.save(account);
    }

    public Account login(Account account) throws InvalidLoginException {
        String username = account.getUsername();
        String password = account.getPassword();
        Optional<Account> found = this.accountRepository.findByUsernameAndPassword(username, password);
        if (found.isEmpty())
            throw new InvalidLoginException();
        return found.get();
    }
}
