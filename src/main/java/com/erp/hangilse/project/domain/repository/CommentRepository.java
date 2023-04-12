package com.erp.hangilse.project.domain.repository;

import com.erp.hangilse.project.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
