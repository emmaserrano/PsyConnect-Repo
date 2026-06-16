package ni.edu.uam.psyconnect_backend.dto;

public class RecoveryCodeRequest {

    private String email;
    private String code;

    public RecoveryCodeRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(
            String code
    ) {
        this.code = code;
    }
}