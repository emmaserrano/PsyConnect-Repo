package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.dto.VerifyCodeRequest;
import ni.edu.uam.psyconnect_backend.model.VerificationCode;
import ni.edu.uam.psyconnect_backend.repository.VerificationCodeRepository;
import ni.edu.uam.psyconnect_backend.service.EmailService;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    private final EmailService emailService;

    private final VerificationCodeRepository repository;

    public VerificationController(
            EmailService emailService,
            VerificationCodeRepository repository
    ) {
        this.emailService = emailService;
        this.repository = repository;
    }

    @PostMapping("/send")
    public String sendCode(
            @RequestParam String email
    ) {

        String code =
                String.format(
                        "%06d",
                        new Random().nextInt(999999)
                );

        VerificationCode verificationCode =
                repository.findByEmail(email);

        if (verificationCode == null) {

            verificationCode =
                    new VerificationCode();

            verificationCode.setEmail(email);
        }

        verificationCode.setCode(code);

        repository.save(
                verificationCode
        );

        emailService.sendVerificationCode(
                email,
                code
        );

        return "Código enviado";
    }
    @PostMapping("/validate")
    public Boolean validateCode(
            @RequestBody
            VerifyCodeRequest request
    ) {

        VerificationCode verificationCode =
                repository.findByEmail(
                        request.getEmail()
                );

        if (verificationCode == null) {

            return false;
        }

        return verificationCode
                .getCode()
                .equals(
                        request.getCode()
                );
    }
}