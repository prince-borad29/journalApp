package net.engineeringdigest.journalApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotEmpty
    @Schema(description = "The User's Username")
    private String username;
    @NotEmpty
    private String password;
    private String email;
    private boolean sentimentAnalysis;
}
