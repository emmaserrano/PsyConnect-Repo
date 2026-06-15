package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.model.Psychologist;
import ni.edu.uam.psyconnect_backend.service.PsychologistService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class AdminController {

    private final PsychologistService service;

    public AdminController(
            PsychologistService service
    ) {
        this.service = service;
    }

    @GetMapping("/admin")
    public String adminPage() {

        return "admin";
    }

    @PostMapping("/admin/save")
    public String savePsychologist(

            Psychologist psychologist,

            @RequestParam("photoFile")
            MultipartFile photoFile

    ) {

        try {

            String fileName =
                    photoFile.getOriginalFilename();

            Path uploadPath =
                    Paths.get("uploads");

            if (!Files.exists(uploadPath)) {

                Files.createDirectories(
                        uploadPath
                );
            }

            Files.copy(
                    photoFile.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            psychologist.setPhoto(
                    fileName
            );

            service.save(
                    psychologist
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "redirect:/admin";
    }
}