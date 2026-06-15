package ni.edu.uam.psyconnect_backend.repository;

import ni.edu.uam.psyconnect_backend.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository
        extends JpaRepository<
        VerificationCode,
        Long
        > {

    VerificationCode findByEmail(
            String email
    );
}