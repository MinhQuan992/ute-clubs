package hcmute.manage.club.uteclubs.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<CustomException> noContentException() {
        return null;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DateException.class, UnderageException.class, PasswordsDoNotMatchException.class,
            OtpException.class, InvalidRequestException.class})
    public ResponseEntity<CustomException> badRequestException(Exception exception) {
        CustomException customException = new CustomException();
        customException.setStatus(400);
        customException.setError("Bad Request");
        customException.setMessage(exception.getMessage());
        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessTokenException.class)
    public ResponseEntity<CustomException> unauthorizedException(Exception exception) {
        CustomException customException = new CustomException();
        customException.setStatus(401);
        customException.setError("Unauthorized");
        customException.setMessage(exception.getMessage());
        return new ResponseEntity<>(customException, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<CustomException> permissionException(Exception exception) {
        CustomException customException = new CustomException();
        customException.setStatus(403);
        customException.setError("Forbidden");
        customException.setMessage(exception.getMessage());
        return new ResponseEntity<>(customException, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomException> notFoundException(Exception exception) {
        CustomException customException = new CustomException();
        customException.setStatus(404);
        customException.setError("Not Found");
        customException.setMessage(exception.getMessage());
        return new ResponseEntity<>(customException, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<CustomException> resourceConflictException(Exception exception) {
        CustomException customException = new CustomException();
        customException.setStatus(409);
        customException.setError("Resource Conflict");
        customException.setMessage(exception.getMessage());
        return new ResponseEntity<>(customException, HttpStatus.CONFLICT);
    }
}
