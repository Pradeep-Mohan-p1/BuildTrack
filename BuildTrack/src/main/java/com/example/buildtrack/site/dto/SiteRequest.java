package com.example.buildtrack.site.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteRequest {

    @NotBlank
    private String siteName;

    @NotBlank
    @Size(max = 500)
    private String address;

    private String city;

    private String state;

    private String pincode;

    @NotNull
    private Long projectId;

    private LocalDate startDate;

    private LocalDate endDate;
}