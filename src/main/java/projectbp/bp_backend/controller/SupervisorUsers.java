package projectbp.bp_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.dao.AgenceRepo;
import projectbp.bp_backend.dto.auth.RegisterRequest;

@RestController
@RequiredArgsConstructor
public class SupervisorUsers {

    private final AgenceRepo agenceRepo;

    @GetMapping("/public/agence")
    public ResponseEntity<Object> getAllAgences() {
        return ResponseEntity.ok(agenceRepo.findAll());
    }
    @PostMapping("/supervisor/saveagence")
    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest agencereq
    ) {
        Agence ag = new Agence();
        ag.setNom(agencereq.getNom());
        return ResponseEntity.ok(agenceRepo.save(ag));
    }
    @GetMapping("/user/alone")
    public ResponseEntity<Object> useralone() {
        return ResponseEntity.ok("User alone can access this api");
    }
    @GetMapping("/auth/supervisoruser/both")
    public ResponseEntity<Object> both() {
        return ResponseEntity.ok("both can access this api");
    }
}
