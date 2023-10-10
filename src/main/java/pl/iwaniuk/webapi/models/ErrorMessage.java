package pl.iwaniuk.webapi.models;

public class ErrorMessage {

    private String errorName;

    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(String errorName, String message) {
        this.errorName = errorName;
        this.message = message;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
