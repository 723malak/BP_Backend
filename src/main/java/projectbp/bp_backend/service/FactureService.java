package projectbp.bp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Demande;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Facture;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dao.FactureRepo;
import projectbp.bp_backend.dto.CRUD.FactureRequest;
import projectbp.bp_backend.service.DemandeService;


import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepo factureRepo;
    private final DevisRepo devisRepo;
    private final DemandeService demandeService;

    public Optional<Facture> findByNumero(String numero) {
        return factureRepo.findByNumero(numero);
    }

    public List<Facture> findAll() {
        return factureRepo.findAll();
    }

    public ResponseEntity<Object> createFacture(FactureRequest factureRequest, User currentUser) throws AccessDeniedException {
        Optional<Facture> existingFacture = factureRepo.findByNumero(factureRequest.getNumero());
        if (existingFacture.isPresent()) {
            return ResponseEntity.badRequest().body("Facture with this Numero already exists");
        }
        Optional<Devis> existingDevis = devisRepo.findByNumero(factureRequest.getDevis().getNumero());
        if (!existingDevis.isPresent()) {
            return ResponseEntity.badRequest().body("Devis not found");
        }
        if (!isValidFactureRequest(factureRequest)) {
            if ("USER".equals(currentUser.getRole())) {
                Demande demande = new Demande();
                demande.setSender(currentUser);
                demande.setErrorMessage("Erreur lors de la saisie des informations de la facture. Veuillez soumettre une demande de modification.");
                demandeService.createDemandeFacture(demande, factureRequest.getNumero(), demande.getErrorMessage());
                return ResponseEntity.badRequest().body(demande.getErrorMessage());
            } else {
                return ResponseEntity.badRequest().body("Erreur lors de la saisie des informations de la facture. Veuillez corriger les champs.");
            }
        }
        Facture facture = new Facture();
        facture.setNumero(factureRequest.getNumero());
        facture.setDate_traitement(new Date());
        facture.setDate_facture(factureRequest.getDate_facture());
        facture.setMontant(factureRequest.getMontant() * 1.2);
        facture.setDevis(existingDevis.get());
        facture.setTraitepar(currentUser);
        try {
            Facture savedFacture = factureRepo.save(facture);
            return ResponseEntity.ok(savedFacture);
        } catch (Exception e) {
            if ("USER".equals(currentUser.getRole())) {
                Demande demande = new Demande();
                demande.setFacture(facture);
                demande.setSender(currentUser);
                demande.setErrorMessage("Erreur lors de la création de la facture. Veuillez soumettre une demande de modification.");
                try {
                    demandeService.createDemandeFacture(demande, facture.getNumero(), demande.getErrorMessage());
                } catch (AccessDeniedException ade) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + ade.getMessage());
                }
                return ResponseEntity.badRequest().body(demande.getErrorMessage());
            } else {
                throw new RuntimeException("Erreur lors de la création de la facture.", e);
            }
        }
    }

    public Facture updateFacture(Long id, FactureRequest factureRequest) {
        Facture existingFacture = factureRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Facture not found with id: " + id));

        if (factureRequest.getNumero() != null) {
            existingFacture.setNumero(factureRequest.getNumero());
        }
        if (factureRequest.getDate_facture() != null) {
            existingFacture.setDate_facture(factureRequest.getDate_facture());
        }
        if (factureRequest.getMontant() != null) {
            existingFacture.setMontant(factureRequest.getMontant() * 1.2);
        }
        if (factureRequest.getDevis() != null) {
            existingFacture.setDevis(factureRequest.getDevis());
        }
        return factureRepo.save(existingFacture);
    }

    public void deleteFacture(Long id) {
        if (!factureRepo.existsById(id)) {
            throw new EntityNotFoundException("Facture not found with id: " + id);
        }
        factureRepo.deleteById(id);
    }

    private boolean isValidFactureRequest(FactureRequest factureRequest) {
        if (factureRequest.getNumero() == null || factureRequest.getNumero().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(factureRequest.getMontant());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
