package pl.dreamcode.errornotifier.users;

import org.springframework.context.ApplicationEvent;

public class OnPasswordResetRequestEvent extends ApplicationEvent {
    private String email;

    public OnPasswordResetRequestEvent(String email) {
        super(email);
        
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
}
