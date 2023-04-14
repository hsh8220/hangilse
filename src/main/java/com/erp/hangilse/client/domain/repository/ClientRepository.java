package com.erp.hangilse.client.domain.repository;

import com.erp.hangilse.client.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @EntityGraph(attributePaths = {"tags"})
    Page<Client> findAll(Pageable pageable);
    @EntityGraph(attributePaths = {"tags"})
    Optional<Client> findById(Long id);
}
