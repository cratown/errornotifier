package pl.dreamcode.errornotifier.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pl.dreamcode.errornotifier.errors.ErrorService;
import pl.dreamcode.errornotifier.BaseRestController;
import pl.dreamcode.errornotifier.errors.ErrorForm;

@RestController("ApiErrorsController")
@RequestMapping("/errors.json")
public class ErrorsController extends BaseRestController {

    @Autowired
    private ErrorService errorService;

    @PostMapping
    public ResponseEntity<String> saveError(@Valid @RequestBody ErrorForm newError) {
        errorService.create(newError.toError());
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }
}
