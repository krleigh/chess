package serverfacade.requestresult;

public class ErrorResult {
    private final int statusCode;
    private final String message;

    public ErrorResult(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}