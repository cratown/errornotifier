package pl.dreamcode.errornotifier.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import pl.dreamcode.errornotifier.errors.ErrorService;
import pl.dreamcode.errornotifier.errors.OldErrorForm;
import pl.dreamcode.errornotifier.BaseRestController;
import pl.dreamcode.errornotifier.errors.ApiErrorForm;

@RestController("ApiErrorsController")
// @RequestMapping("/api")
public class ErrorsController extends BaseRestController {

    @Autowired
    private ErrorService errorService;

    @PostMapping(value = "/api/errors.json")
    @Operation(
        tags = {"Errors"},
        description = "Action to store error notification from project.",
        responses = {@ApiResponse(
            content={
                @Content(
                    mediaType = "text/plain",
                    examples = {
                    @ExampleObject(value = "OK")
                })
            }, 
            responseCode="200"
        )}
    )
    public ResponseEntity<String> saveError(@Valid @RequestBody ApiErrorForm newError) {
        errorService.create(newError.toError());
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @RequestMapping(
        value = "/errors.json", 
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Deprecated
    @Operation(
        tags = {"Errors"},
        description = "Endpoint for old version of error notification request."
    )
    public ResponseEntity<String> testData(@Valid @ModelAttribute OldErrorForm newError) {
        errorService.create(newError.toError());
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }
}
