package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.model.Psychologist;
import ni.edu.uam.psyconnect_backend.service.PsychologistService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
            @ModelAttribute Psychologist psychologist
    ) {

        service.save(psychologist);

        return "redirect:/admin";
    }
}