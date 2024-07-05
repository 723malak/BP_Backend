package projectbp.bp_backend.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.dao.AgenceRepo;
import projectbp.bp_backend.dto.auth.RegisterRequest;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AgenceController {

    private final AgenceRepo agenceRepo;

    @GetMapping("/public/agence")
    public ResponseEntity<Object> getAllAgences() {
        return ResponseEntity.ok(agenceRepo.findAll());
    }

    @GetMapping("/public/agence/nom/{nom}")
    public Optional<Agence> findByNom(@PathVariable  String nom) {
        return agenceRepo.findByNom(nom);
    }
    @PostMapping("/supervisor/saveagence")
    public ResponseEntity<Object> createAg(
            @RequestBody RegisterRequest agencereq
    ) {
        Agence ag = new Agence();
        ag.setNom(agencereq.getNom());
        return ResponseEntity.ok(agenceRepo.save(ag));
    }
    @PutMapping("/supervisor/saveagence/{id}")
    public ResponseEntity<Agence> updateAgence(
            @PathVariable Long id,
            @RequestBody RegisterRequest agencereq
    ) {
        Agence existingAgence = agenceRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agence non trouvée avec l'ID : " + id));
        existingAgence.setNom(agencereq.getNom());
        Agence updatedAgence = agenceRepo.save(existingAgence);
        return ResponseEntity.ok(updatedAgence);
    }
    @DeleteMapping("/supervisor/deleteagence/{id}")
    public ResponseEntity<Void> deleteAgence(@PathVariable Long id) {
        if (!agenceRepo.existsById(id)) {
            throw new EntityNotFoundException("Agence non trouvée avec l'ID : " + id);
        }
        agenceRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

        @GetMapping("/user/alone")
    public ResponseEntity<Object> useralone() {
        return ResponseEntity.ok("User alone can access this api");
    }

}
