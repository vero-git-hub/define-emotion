package com.example.defineemotion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditProfileDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be in a valid format")
    private String email;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "City is required")
    private String city;
}