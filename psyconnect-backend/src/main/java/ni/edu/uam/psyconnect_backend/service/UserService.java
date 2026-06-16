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
            throw new RuntimeException(
                    "El correo ya está registrado"
            );
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException(
                    "El nombre de usuario ya está en uso"
            );
        }

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        String loginValue =
                request.getEmail();

        User user =
                userRepository
                        .findByEmail(loginValue)
                        .orElse(
                                userRepository
                                        .findByUsername(loginValue)
                                        .orElse(null)
                        );

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
    public User getUserByUsername(
            String username
    ) {

        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Usuario no encontrado"
                        )
                );
    }
    public User updateUser(Long id, User updatedUser) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setName(updatedUser.getName());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setAge(updatedUser.getAge());

        return userRepository.save(user);
    }

    public void resetPassword(
            String email,
            String newPassword
    ) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Usuario no encontrado"
                                )
                        );

        user.setPassword(newPassword);

        userRepository.save(user);
    }

}