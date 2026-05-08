package io.github.speranskyatryom.taxi_microservices.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRegistrationRequest {
    @NotBlank(message = "Firs name is required")
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(max = 100)
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Size(max = 20)
    @Pattern(
            regexp = "^\\+?[1-9]\\d{10,14}$",
            message = "Invalid phone number format"
    )
    private String phone;
}
