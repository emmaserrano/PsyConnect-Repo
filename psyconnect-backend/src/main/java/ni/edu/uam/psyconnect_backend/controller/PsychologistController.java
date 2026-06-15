package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.model.Psychologist;
import ni.edu.uam.psyconnect_backend.service.PsychologistService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/psychologists")
public class PsychologistController {

    private final PsychologistService service;

    public PsychologistController(
            PsychologistService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<Psychologist> getAll() {

        return service.getAll();
    }
}