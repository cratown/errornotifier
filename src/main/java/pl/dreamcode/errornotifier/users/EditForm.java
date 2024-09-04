package pl.dreamcode.errornotifier.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Object for edit page
public class EditForm {

    private Long id;

    @NotNull
    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") // Email validation with custom, more restrict pattern
    private String email;

    // Object can only exists with ID
    public EditForm(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    
}
