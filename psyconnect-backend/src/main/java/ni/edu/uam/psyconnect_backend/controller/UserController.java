package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.dto.LoginRequest;
import ni.edu.uam.psyconnect_backend.dto.LoginResponse;
import ni.edu.uam.psyconnect_backend.model.User;
import ni.edu.uam.psyconnect_backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(
            @RequestBody User user
    ) {

        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {

        return userService.login(request);
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}