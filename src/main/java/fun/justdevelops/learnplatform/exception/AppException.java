package fun.justdevelops.learnplatform.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final int httpCode;

    public AppException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }
}
