package ni.edu.uam.psyconnect_backend.dto;

public class LoginResponse {

    private boolean success;

    private String message;

    private Long userId;

    public LoginResponse() {
    }

    public LoginResponse(
            boolean success,
            String message,
            Long userId
    ) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}