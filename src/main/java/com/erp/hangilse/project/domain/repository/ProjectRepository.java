package com.erp.hangilse.project.domain.repository;

import com.erp.hangilse.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = {"account", "tag", "client", "watchers", "comments"})
    Page<Project> findByStatus(String status, Pageable pageable);

    @EntityGraph(attributePaths = {"account", "tag", "client", "watchers", "comments"})
    List<Project> findByCreateTimeBetween(LocalDate from, LocalDate to);

    void deleteById(Long id);
}
