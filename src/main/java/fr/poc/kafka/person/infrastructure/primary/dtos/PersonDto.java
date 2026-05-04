package fr.poc.kafka.person.infrastructure.primary.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public record PersonDto(
        Long personId,
        @NotBlank(message = "firstname is mandatory")
        String firstname,
        @NotBlank(message = "lastname is mandatory")
        String lastname,
        @Min(1)
        @Max(99)
        int age
) {
}
