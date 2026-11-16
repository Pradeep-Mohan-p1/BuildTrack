package com.example.buildtrack.audit.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.example.buildtrack.audit.entity.AuditLog;
import com.example.buildtrack.audit.service.AuditService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return auditService.getAllAuditLogs(pageable);
    }

    @GetMapping("/user/{username}")
    public Page<AuditLog> getAuditLogsByUsername(
            @PathVariable String username,
            Pageable pageable) {

        return auditService.getAuditLogsByUsername(username, pageable);
    }

    @GetMapping("/action/{action}")
    public Page<AuditLog> getAuditLogsByAction(
            @PathVariable String action,
            Pageable pageable) {

        return auditService.getAuditLogsByAction(action, pageable);
    }
}