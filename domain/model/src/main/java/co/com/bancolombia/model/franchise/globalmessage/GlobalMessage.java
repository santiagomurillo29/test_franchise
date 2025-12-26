package co.com.bancolombia.model.franchise.globalmessage;

public enum GlobalMessage {

    BAD_PARAMETER(GlobalMessage.STATUS_CODE_400, "The name is already registered"),
    DATABASE_ERROR(GlobalMessage.STATUS_CODE_500, "Database is down");

    public static final String STATUS_CODE_400 = "400";
    public static final String STATUS_CODE_500 = "500";

    private final String statusCode;
    private final String message;

    GlobalMessage(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
