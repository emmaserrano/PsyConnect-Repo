package ni.edu.uam.psyconnect_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class VerificationCode {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private String email;

    private String code;

    private LocalDateTime expirationDate;

    public VerificationCode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(
            Long id
    ) {
        this.id = id;
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

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(
            LocalDateTime expirationDate
    ) {
        this.expirationDate = expirationDate;
    }
}