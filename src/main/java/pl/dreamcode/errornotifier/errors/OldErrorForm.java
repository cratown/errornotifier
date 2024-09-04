package pl.dreamcode.errornotifier.errors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OldErrorForm  implements ErrorForm {

    @NotBlank
    private String project;

    @NotNull
    private String text;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    @Override
    public Error toError() {
        Error error = new Error();
        error.setProjectName(project);
        error.setBody(text);
        return error;
    }

}
