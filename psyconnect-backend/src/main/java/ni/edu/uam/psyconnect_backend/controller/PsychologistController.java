package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.model.Psychologist;
import ni.edu.uam.psyconnect_backend.service.PsychologistService;

import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/featured")
    public List<Psychologist> getFeatured() {

        return service.getFeatured();
    }

    @PostMapping
    public Psychologist save(
            @RequestBody Psychologist psychologist
    ) {

        return service.save(psychologist);
    }

}