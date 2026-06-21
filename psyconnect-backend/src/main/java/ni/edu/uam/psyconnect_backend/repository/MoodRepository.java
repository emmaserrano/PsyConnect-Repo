package ni.edu.uam.psyconnect_backend.repository;

import ni.edu.uam.psyconnect_backend.model.Mood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

public interface MoodRepository
        extends JpaRepository<Mood, Long> {

    Optional<Mood> findByUserIdAndDate(
            Long userId,
            LocalDate date
    );

    List<Mood> findByUserIdOrderByDateDesc(
            Long userId
    );
}