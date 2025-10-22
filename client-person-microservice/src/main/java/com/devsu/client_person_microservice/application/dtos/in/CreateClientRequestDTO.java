package com.devsu.client_person_microservice.application.dtos.in;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClientRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters long")
    private String name;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(?i)(MALE|FEMALE|OTHER)$", message = "Gender must be 'MALE', 'FEMALE', or 'OTHER'")
    private String gender;

    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 120, message = "Age cannot be greater than 120")
    private int age;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters long")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9\\-\\+]{7,15}$", message = "Phone number must be valid and contain 7 to 15 digits")
    private String phoneNumber;

    @NotBlank(message = "Identification is required")
    @Size(min = 5, max = 30, message = "Identification must be between 5 and 30 characters long")
    private String identification;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters long")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\-_])[A-Za-z\\d@$!%*?&\\-_]{8,}$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;
}