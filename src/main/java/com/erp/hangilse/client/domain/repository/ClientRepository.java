package com.erp.hangilse.client.domain.repository;

import com.erp.hangilse.client.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
