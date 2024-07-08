package projectbp.bp_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import projectbp.bp_backend.bean.Demande;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.service.DemandeService;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DemandeController {

    @GetMapping("user/demandes_User")
    public ResponseEntity<List<Demande>> getDemandesBySender(@PathVariable String senderName) {
        List<Demande> demandes = demandeservice.getDemandesBySender(senderName);
        return ResponseEntity.ok(demandes);
    }
    @GetMapping("supervisor/Alldemandes")
    public ResponseEntity<List<Demande>> getAllDemandes() {
        List<Demande> demandes = demandeservice.getAllDemandes();
        return ResponseEntity.ok(demandes);
    }

    @PostMapping("user/savedemande")
    public ResponseEntity<Demande> createDemandeFacture(@RequestBody Demande demande, @PathVariable String factureNumero, @PathVariable String errorMessage) throws AccessDeniedException {
        Demande createdDemande = demandeservice.createDemandeFacture(demande, factureNumero, errorMessage);
        return ResponseEntity.ok(createdDemande);
    }

    @GetMapping("supervisor/approve_demande")
    public ResponseEntity<Void> approveDemande(@PathVariable Long demandeId) {
        demandeservice.approveDemande(demandeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("public/supervisor")
    public User findSupervisor() {
        return demandeservice.findSupervisor();
    }

    private final DemandeService demandeservice;
}
