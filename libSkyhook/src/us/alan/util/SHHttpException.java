package us.alan.util;

public class SHHttpException extends RuntimeException {

    public SHHttpException(String errorMessage) {
        super(errorMessage, new RuntimeException());
    }

    public SHHttpException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}