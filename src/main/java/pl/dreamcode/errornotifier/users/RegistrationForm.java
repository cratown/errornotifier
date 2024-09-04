package pl.dreamcode.errornotifier.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.dreamcode.errornotifier.users.validation.PasswordMatches;

@PasswordMatches
public class RegistrationForm {    
    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String matchingPassword;
    
    @NotNull
    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") // Email validation with custom, more restrict pattern
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
