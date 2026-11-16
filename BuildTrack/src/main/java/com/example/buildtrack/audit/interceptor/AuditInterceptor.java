package com.example.buildtrack.audit.interceptor;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.buildtrack.audit.entity.AuditLog;
import com.example.buildtrack.audit.service.AuditService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditInterceptor implements HandlerInterceptor {

    private static final System.Logger LOGGER =
            System.getLogger(AuditInterceptor.class.getName());

    private final AuditService auditService;

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception) {

        try {
            String username = request.getRemoteUser();
            if (username == null || username.isBlank()) {
                username = "ANONYMOUS";
            }

            AuditLog auditLog = AuditLog.builder()
                    .username(username)
                    .action(request.getMethod())
                    .endpoint(request.getRequestURI())
                    .httpMethod(request.getMethod())
                    .responseStatus(response.getStatus())
                    .ipAddress(request.getRemoteAddr())
                    .timestamp(LocalDateTime.now())
                    .build();

            auditService.saveAuditLog(auditLog);

        } catch (Exception ex) {
            LOGGER.log(
                    System.Logger.Level.WARNING,
                    "Failed to write API audit log",
                    ex);
        }
    }
}