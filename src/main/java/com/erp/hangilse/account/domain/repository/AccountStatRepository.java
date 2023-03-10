package com.erp.hangilse.account.domain.repository;

import com.erp.hangilse.account.domain.AccountStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AccountStatRepository extends JpaRepository<AccountStat, Long> {
    List<AccountStat> findAllByDateBetween(LocalDate from, LocalDate to);
}
