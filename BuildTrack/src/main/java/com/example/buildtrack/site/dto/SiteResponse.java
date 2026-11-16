package com.example.buildtrack.site.dto;

import java.time.LocalDate;

import com.example.buildtrack.site.entity.SiteStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SiteResponse {

    private Long id;

    private String siteName;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private Long projectId;

    private String projectName;

    private LocalDate startDate;

    private LocalDate endDate;

    private SiteStatus status;
}