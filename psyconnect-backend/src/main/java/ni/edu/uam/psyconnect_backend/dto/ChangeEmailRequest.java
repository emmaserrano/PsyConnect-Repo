package ni.edu.uam.psyconnect_backend.dto;

public class ChangeEmailRequest {

    private Long userId;

    private String newEmail;

    public ChangeEmailRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(
            Long userId
    ) {
        this.userId = userId;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(
            String newEmail
    ) {
        this.newEmail = newEmail;
    }
}