package pl.dreamcode.errornotifier.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.dreamcode.errornotifier.users.validation.PasswordMatches;

@PasswordMatches
public class PasswordChangeForm implements PasswordMatchesForm {
    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String matchingPassword;

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
}
