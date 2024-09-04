package pl.dreamcode.errornotifier.errors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ApiErrorForm implements ErrorForm {

    @NotBlank
    private String projectName;

    @NotNull
    private String body;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Error toError() {
        Error error = new Error();
        error.setProjectName(projectName);
        error.setBody(body);
        return error;
    }
}
