package ExceptionHandler;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ErrorResponse {
    private String errorcode;
    private String errormessage;

    public ErrorResponse(String errorcode, String message) {
        this.errorcode = errorcode;
        this.errormessage = message;
    }
}
