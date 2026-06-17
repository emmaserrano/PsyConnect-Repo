package ni.edu.uam.psyconnect_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(
            JavaMailSender mailSender
    ) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(
            String email,
            String code
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "Código de verificación - PsyConnect"
        );

        message.setText(
                "Tu código de verificación es: "
                        + code
        );

        mailSender.send(message);
    }
}