package pl.dreamcode.errornotifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class BaseRestController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleInValidInput(MethodArgumentNotValidException ex) {
        Map<String,String> errorMap = ex.getAllErrors()
                .stream()
                .collect(Collectors.toMap(x -> ((FieldError)x).getField(), 
                    b -> b.getDefaultMessage(),(p,q) -> p, LinkedHashMap::new));
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}
