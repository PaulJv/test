package wedoogift.level1.common.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wedoogift.level1.model.dto.ErrorResponseDTO;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerRest extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { UserNotFoundException.class, CompanyNotFoundException.class })
    protected ResponseEntity<ErrorResponseDTO> handleNotFoundException(RuntimeException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { CompanyNotEnoughBalanceException.class })
    protected ResponseEntity<ErrorResponseDTO> handleCompanyNotEnoughBalanceException(RuntimeException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = { VoucherTypeUnknownException.class })
    protected ResponseEntity<ErrorResponseDTO> handleVoucherTypeUnknownException(RuntimeException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
