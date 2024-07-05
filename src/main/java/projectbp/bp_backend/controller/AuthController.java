package projectbp.bp_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dto.auth.RegisterRequest;
import projectbp.bp_backend.service.AuthenticationUserService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {



    private final AuthenticationUserService authservice;

    @PostMapping("/register")
    public ResponseEntity<RegisterRequest> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authservice.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<RegisterRequest> login(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authservice.login(request));
    }
    @PostMapping("/refresh")
    public ResponseEntity<RegisterRequest> refreshToken(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authservice.refreshToken(request));
    }
    @GetMapping("/current-user")
    public ResponseEntity<Object> getCurrentUser() {
        try {
            UserDetails user = authservice.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving current user: " + e.getMessage());
        }
    }


}
