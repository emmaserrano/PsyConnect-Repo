package ni.edu.uam.psyconnect_backend.repository;

import ni.edu.uam.psyconnect_backend.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository
        extends JpaRepository<Achievement, Long> {

    List<Achievement> findByUserId(
            Long userId
    );

    boolean existsByUserIdAndTitle(
            Long userId,
            String title
    );
}