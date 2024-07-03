package projectbp.bp_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
