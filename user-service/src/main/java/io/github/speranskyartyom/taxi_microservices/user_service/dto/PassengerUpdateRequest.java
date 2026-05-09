package io.github.speranskyartyom.taxi_microservices.user_service.dto;

import jakarta.validation.constraints.Email;
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
public class PassengerUpdateRequest {
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Size(max = 100, message = "Email must be less than 100 characters")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 20, message = "Phone must be less than 20 characters")
    @Pattern(
            regexp = "^\\+?[1-9]\\d{10,14}$",
            message = "Invalid phone number format"
    )
    private String phone;
}
