package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.dto.LoginRequest;
import ni.edu.uam.psyconnect_backend.dto.LoginResponse;
import ni.edu.uam.psyconnect_backend.model.User;
import ni.edu.uam.psyconnect_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody User user
    ) {

        try {

            User savedUser = userService.saveUser(user);

            return ResponseEntity.ok(savedUser);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {

        return userService.login(request);
    }

    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable Long id
    ) {

        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(
            @PathVariable String email
    ) {

        return userService.getUserByEmail(email);
    }
    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestBody User user
    ) {

        return userService.updateUser(
                id,
                user
        );
    }
}