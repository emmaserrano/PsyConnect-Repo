package ni.edu.uam.psyconnect_backend.service;

import ni.edu.uam.psyconnect_backend.model.Achievement;
import ni.edu.uam.psyconnect_backend.repository.AchievementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementService {

    private final AchievementRepository repository;

    public AchievementService(
            AchievementRepository repository
    ) {
        this.repository = repository;
    }

    public void unlock(
            Long userId,
            String title,
            String description
    ) {

        if (
                repository.existsByUserIdAndTitle(
                        userId,
                        title
                )
        ) {
            return;
        }

        Achievement achievement =
                new Achievement();

        achievement.setUserId(userId);
        achievement.setTitle(title);
        achievement.setDescription(description);

        repository.save(
                achievement
        );
    }

    public List<Achievement> getAchievements(
            Long userId
    ) {
        return repository.findByUserId(
                userId
        );
    }
}