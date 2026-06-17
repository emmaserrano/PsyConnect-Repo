package ni.edu.uam.psyconnect_backend.repository;

import ni.edu.uam.psyconnect_backend.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository
        extends JpaRepository<VerificationCode, Long> {

    Optional<VerificationCode> findByEmail(
            String email
    );
}