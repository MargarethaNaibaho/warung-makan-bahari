package com.enigmacamp.springbootwmbreview.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCustomerRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "Phone Number is required")
    @Size(min = 11, max = 15, message = "Invalid phone number")
    private String phoneNumber;
}