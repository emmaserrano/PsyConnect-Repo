package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.dto.VerifyCodeRequest;
import ni.edu.uam.psyconnect_backend.service.VerificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(
            VerificationService verificationService
    ) {
        this.verificationService = verificationService;
    }

    @PostMapping("/send")
    public String sendCode(
            @RequestParam String email
    ) {

        verificationService.sendCode(email);

        return "Código enviado";
    }

    @PostMapping("/validate")
    public boolean validateCode(
            @RequestBody VerifyCodeRequest request
    ) {

        return verificationService.validateCode(
                request.getEmail(),
                request.getCode()
        );
    }
}