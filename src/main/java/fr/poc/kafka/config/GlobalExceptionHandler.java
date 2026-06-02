package fr.poc.kafka.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ValidationException.class)
//    public Map<String, String> handleValidationExceptions(
//            ValidationException ex) {
//        Map<String, String> errors = new HashMap<>();
////        ex.getBindingResult().getAllErrors().forEach((error) -> {
////            String fieldName = ((FieldError) error).getField();
////            String errorMessage = error.getDefaultMessage();
////            errors.put(fieldName, errorMessage);
////        });
//        return errors;
//    }

@ExceptionHandler(Exception.class)
public ResponseEntity<String> handleGenericException(Exception exception) {
//    final CrmErrorMessage errorMessage = CrmErrorMessage.INTERNAL_SERVER_ERROR;
//    final HttpStatus status = CrmErrorMessage.getHttpStatus(errorMessage);
    final String body = "erreur ! heu";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
}
}
