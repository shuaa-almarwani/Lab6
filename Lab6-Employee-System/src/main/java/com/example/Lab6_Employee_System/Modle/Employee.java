package com.example.Lab6_Employee_System.Modle;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Employee {
    @NotEmpty(message = "id should not be empty")
    @Size(min = 3, message = "id must be more than 2 characters")
    private String ID;
    @NotEmpty(message = "name should not be empty")
    @NotBlank(message = "name cannot be empty")
    @Size(min = 5, message = "name must be more than 4 characters")
    @Pattern(regexp = "^[\\p{L} ]+$", message ="name must contains only characters (no number )" )
    private String name;
    @Email(message = "must be a valid email format ")
    private String email;
    @Size(min = 10, max = 10, message = "Phone Number must consist of exactly 10 digits")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone Number must start with 'O5 '")
    private String phoneNumber;
    @NotNull(message = "age cannot be empty")
    @Positive
    @Min(value = 26, message = "age must be more than 25")
    private int age;
    @NotEmpty(message = "position cannot be empty")
    @Pattern(regexp = "^(supervisor|coordinator)$",
            message = "position must be either  'supervisor' or 'coordinator' only")
    private String position ;
    private boolean onLeave = false;
    @NotNull(message = "hireDate cannot be empty")
    @PastOrPresent(message = "hire Date should be a date in the present or the past")
    private Date hireDate;
    @NotNull(message = "annual Leave cannot be empty")
    @PositiveOrZero(message = "annualLeave must be 0 or more")
    private int annualLeave;

}
