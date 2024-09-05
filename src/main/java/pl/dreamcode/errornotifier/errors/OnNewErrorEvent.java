package pl.dreamcode.errornotifier.errors;

import org.springframework.context.ApplicationEvent;

public class OnNewErrorEvent extends ApplicationEvent {

    private Error projectError;

    public OnNewErrorEvent(Error projectError) {
        super(projectError);
        
        this.projectError = projectError;
    }

    public Error getProjectError() {
        return projectError;
    }

}
