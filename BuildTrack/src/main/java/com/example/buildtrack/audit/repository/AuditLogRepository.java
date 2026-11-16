package com.example.buildtrack.audit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.buildtrack.audit.entity.AuditLog;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByUsernameContainingIgnoreCase(
            String username, Pageable pageable);

    Page<AuditLog> findByActionContainingIgnoreCase(
            String action, Pageable pageable);
}