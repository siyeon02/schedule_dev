package ExceptionHandler;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ErrorResponse {
    private String errormessage;
    private int errorcode;

    public ErrorResponse(String message, int errorcode) {
        this.errormessage = message;
        this.errorcode = errorcode;
    }
}
