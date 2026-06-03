package ni.edu.uam.psyconnect_backend.service;

import ni.edu.uam.psyconnect_backend.dto.LoginRequest;
import ni.edu.uam.psyconnect_backend.dto.LoginResponse;
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

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {

            return new LoginResponse(
                    false,
                    "Usuario no encontrado",
                    null
            );
        }

        if (!user.getPassword().equals(request.getPassword())) {

            return new LoginResponse(
                    false,
                    "Contraseña incorrecta",
                    null
            );
        }

        return new LoginResponse(
                true,
                "Inicio de sesión exitoso",
                user.getId()
        );
    }
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}