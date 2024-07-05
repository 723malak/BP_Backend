package projectbp.bp_backend.service;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dto.CRUD.DevisRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DevisService {

    public Optional<Devis> findByNumero(String numero) {
        return devis_repo.findByNumero(numero);
    }

    public List<Devis> findAll() {
        return devis_repo.findAll();
    }

    public ResponseEntity<Object> createDevis(DevisRequest devisRequest) {
        User user = authenticationUserService.getCurrentUser();
        Optional<Devis> existingDevis = devis_repo.findByNumero(devisRequest.getNumero());
        if (existingDevis.isPresent()) {
            return null;
        }
        Devis devis = new Devis();
        devis.setNumero(devisRequest.getNumero());
        devis.setDate(new Date());
        devis.setEquipementE(devisRequest.getEquipementE());
        devis.setPrestataire(devisRequest.getPrestataire());
        devis.setMontant(devisRequest.getMontant());
        devis.setAssurance(devisRequest.getAssurance());
        devis.setTechnicien(devisRequest.getTechnicien());
        devis.setTraitepar(user);
        if (devisRequest.getPourcentageTTC() != null) {
            double montantTTC = devisRequest.getMontant() * (1 + devisRequest.getPourcentageTTC() / 100);
            devis.setMontantTTC(montantTTC);
        }
        Devis savedDevis = devis_repo.save(devis);
        return ResponseEntity.ok(savedDevis);
    }
    private final AuthenticationUserService authenticationUserService;
    private final DevisRepo devis_repo;
}
