package ExceptionHandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Getter
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex) {
        ErrorResponse response = new ErrorResponse("ERR001", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorResponse> handleExpiredTokenException(ExpiredTokenException ex) {
        ErrorResponse response = new ErrorResponse("ERR002", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnsupportedTokenException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedTokenException(UnsupportedTokenException ex) {
        ErrorResponse response = new ErrorResponse("ERR003", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyClaimsException.class)
    public ResponseEntity<ErrorResponse> handleEmptyClaimsException(EmptyClaimsException ex) {
        ErrorResponse response = new ErrorResponse("ERR004", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 추가적인 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse response = new ErrorResponse("ERR500", "네트워크 요청에 실패했습니다. 다시 시도해주시기 바랍니다.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }
    }

    public static class ExpiredTokenException extends RuntimeException {
        public ExpiredTokenException(String message) {
            super(message);
        }
    }

    public static class UnsupportedTokenException extends RuntimeException {
        public UnsupportedTokenException(String message) {
            super(message);
        }

    }

    public static class EmptyClaimsException extends RuntimeException {
        public EmptyClaimsException(String message) {
            super(message);
        }
    }
}
