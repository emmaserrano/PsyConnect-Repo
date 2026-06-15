package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.dto.VerifyCodeRequest;
import ni.edu.uam.psyconnect_backend.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(
            VerificationService verificationService
    ) {
        this.verificationService =
                verificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendCode(
            @RequestParam String email
    ) {

        verificationService.sendCode(email);

        return ResponseEntity.ok(
                "Código enviado"
        );
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateCode(
            @RequestBody VerifyCodeRequest request
    ) {

        boolean valid =
                verificationService.validateCode(
                        request.getEmail(),
                        request.getCode()
                );

        return ResponseEntity.ok(valid);
    }
}