package projectbp.bp_backend.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import projectbp.bp_backend.bean.Demande;
import projectbp.bp_backend.bean.Facture;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.FactureRepo;
import projectbp.bp_backend.dto.CRUD.FactureRequest;
import projectbp.bp_backend.service.DemandeService;
import projectbp.bp_backend.service.FactureService;


import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class FactureController {


    @GetMapping("/public/facture/numero/{numero}")
    public Optional<Facture> findByNumero(@PathVariable String numero) {
        return factureService.findByNumero(numero);
    }


    @GetMapping("/public/facture")
    public ResponseEntity<Object> getAllFacture() {
        return ResponseEntity.ok(factureService.findAll());
    }


    @PostMapping("/public/saveFacture")
    public ResponseEntity<?> createFacture(@RequestBody FactureRequest factureRequest, Principal principal) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();

        try {
            ResponseEntity<Object> createdFacture = factureService.createFacture(factureRequest, currentUser);
            return ResponseEntity.ok(createdFacture);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/supervisor/updateFacture/{id}")
    public ResponseEntity<Facture> updateFacture(@PathVariable Long id, @RequestBody FactureRequest factureRequest) {
        Facture updatedFacture = factureService.updateFacture(id, factureRequest);
        return ResponseEntity.ok(updatedFacture);
    }

    @PostMapping("/supervisor/deleteFacture/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        factureService.deleteFacture(id);
        return ResponseEntity.noContent().build();
    }

    private final FactureService factureService;
}
