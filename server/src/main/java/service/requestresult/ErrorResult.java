package service.requestresult;

public class ErrorResult {
    private int statusCode;
    private String message;

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