package ni.edu.uam.psyconnect_backend.service;

import ni.edu.uam.psyconnect_backend.model.User;
import ni.edu.uam.psyconnect_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}