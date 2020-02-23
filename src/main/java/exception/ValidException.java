package exception;

public class ValidException extends Exception {

    private String message;


    public ValidException(String message) {
        super(message);
    }
}
