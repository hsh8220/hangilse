package com.erp.hangilse.account.service;

import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.account.domain.AccountAdapter;
import com.erp.hangilse.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account addAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Account changePassword(String email, String password) {
        Account account = this.getAccountByEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        return accountRepository.save(account);
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).get();
    }

    public Account getAccountByName(String name) {
        return accountRepository.findByName(name).get();
    }

    @Transactional
    public long deleteAccountByEmail(String email) {
        Account account = this.getAccountByEmail(email);
        accountRepository.deleteById(account.getId());
        return account.getId();
    }

    public boolean checkDuplicateEmail(String email) {
        Account account;
        try{
            account = accountRepository.findByEmail(email).get();
            return false;
        }catch (NoSuchElementException e) {
            return true;
        }
    }

    public boolean checkDuplicateName(String name) {
        Account account;
        try{
            account = accountRepository.findByName(name).get();
            return false;
        }catch (NoSuchElementException e) {
            return true;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));

        if(!account.isActivated()) throw new RuntimeException(account.getName() + " -> 활성화되어 있지 않습니다.");
        return new AccountAdapter(account);
    }

}
