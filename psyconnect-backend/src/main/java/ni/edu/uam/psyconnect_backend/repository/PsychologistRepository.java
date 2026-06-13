package ni.edu.uam.psyconnect_backend.repository;

import ni.edu.uam.psyconnect_backend.model.Psychologist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsychologistRepository
        extends JpaRepository<Psychologist, Long> {

    List<Psychologist> findByFeaturedTrue();

}