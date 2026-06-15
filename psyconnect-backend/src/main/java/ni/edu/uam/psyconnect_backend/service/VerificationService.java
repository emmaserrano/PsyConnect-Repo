package ni.edu.uam.psyconnect_backend.service;

import ni.edu.uam.psyconnect_backend.model.VerificationCode;
import ni.edu.uam.psyconnect_backend.repository.VerificationCodeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationService {

    private final VerificationCodeRepository repository;
    private final EmailService emailService;

    public VerificationService(
            VerificationCodeRepository repository,
            EmailService emailService
    ) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public void sendCode(String email) {

        String code =
                String.format(
                        "%06d",
                        new Random().nextInt(999999)
                );

        Optional<VerificationCode> existing =
                repository.findByEmail(email);

        VerificationCode verificationCode;

        if (existing.isPresent()) {

            verificationCode = existing.get();

        } else {

            verificationCode = new VerificationCode();

            verificationCode.setEmail(email);
        }

        verificationCode.setCode(code);

        verificationCode.setExpirationDate(
                LocalDateTime.now().plusMinutes(10)
        );

        repository.save(verificationCode);

        emailService.sendVerificationCode(
                email,
                code
        );
    }

    public boolean validateCode(
            String email,
            String code
    ) {

        Optional<VerificationCode> optional =
                repository.findByEmail(email);

        if (optional.isEmpty()) {

            return false;
        }

        VerificationCode savedCode =
                optional.get();

        if (!savedCode.getCode().equals(code)) {

            return false;
        }

        if (
                savedCode.getExpirationDate()
                        .isBefore(LocalDateTime.now())
        ) {

            return false;
        }

        return true;
    }
}