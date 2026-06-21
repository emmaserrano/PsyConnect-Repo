package ni.edu.uam.psyconnect_backend.service;

import ni.edu.uam.psyconnect_backend.model.Achievement;
import ni.edu.uam.psyconnect_backend.repository.AchievementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AchievementService {

    private final AchievementRepository repository;

    public AchievementService(
            AchievementRepository repository
    ) {
        this.repository = repository;
    }

    public Achievement unlock(
            Long userId,
            String badge,
            String description
    ) {

        if (
                repository.existsByUserIdAndBadge(
                        userId,
                        badge
                )
        ) {
            return null;
        }

        Achievement achievement =
                new Achievement(
                        null,
                        userId,
                        badge,
                        description,
                        LocalDate.now()
                );

        return repository.save(
                achievement
        );
    }

    public List<Achievement> getUserAchievements(
            Long userId
    ) {
        return repository.findByUserId(
                userId
        );
    }
}
