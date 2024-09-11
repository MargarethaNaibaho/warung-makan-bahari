package com.enigmacamp.springbootwmbreview.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewMenuRequest {
    @NotBlank(message = "Menu name can't be blank!")
    private String name;

    @NotNull
    @Min(value = 0, message = "Menu price must be greater than or equal to zero")
    private Long price;
}
