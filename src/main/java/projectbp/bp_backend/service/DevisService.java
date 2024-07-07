package projectbp.bp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Technicien;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.AgenceRepo;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dao.TechnicienRepo;
import projectbp.bp_backend.dto.CRUD.DevisRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;


@Service
@RequiredArgsConstructor
public class DevisService {

    private final AuthenticationUserService authenticationUserService;
    private final DevisRepo devis_repo;
    private final AgenceRepo agenceRepo;
    private final TechnicienRepo techRepo;

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
            return ResponseEntity.badRequest().body("Devis with this numero already exists");
        }
        Optional<Technicien> existingTechnicien = techRepo.findByNom(devisRequest.getTechnicien().getNom());
        if (!existingTechnicien.isPresent()) {
            return ResponseEntity.badRequest().body("Technicien not found");
        }

        Optional<Agence> existingAgence = agenceRepo.findByNom(devisRequest.getAgence().getNom());
        if (!existingAgence.isPresent()) {
            return ResponseEntity.badRequest().body("Agence not found");
        }


        Devis devis = new Devis();
        devis.setNumero(devisRequest.getNumero());
        devis.setDate(new Date());
        devis.setEquipementE(devisRequest.getEquipementE());
        devis.setPrestataire(devisRequest.getPrestataire());
        devis.setMontant((devisRequest.getMontant() * 1.2));
        devis.setAssurance(devisRequest.getAssurance());
        devis.setTechnicien(existingTechnicien.get());
        devis.setAgence(existingAgence.get());
        devis.setRejected(devisRequest.getRejected());
        devis.setTraitepar(user);
        return ResponseEntity.ok(devis_repo.save(devis));
    }


    public Devis updateDevis(Long id, DevisRequest devisRequest) {
        Devis existingDevis = devis_repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Devis non trouvé avec l'ID : " + id));

        if (devisRequest.getNumero() != null) {
            existingDevis.setNumero(devisRequest.getNumero());
        }
        if (devisRequest.getDate() != null) {
            existingDevis.setDate(devisRequest.getDate());
        }
        if (devisRequest.getEquipementE() != null) {
            existingDevis.setEquipementE(devisRequest.getEquipementE());
        }
        if (devisRequest.getPrestataire() != null) {
            existingDevis.setPrestataire(devisRequest.getPrestataire());
        }
        if (devisRequest.getMontant() != null) {
            existingDevis.setMontant(devisRequest.getMontant() * 1.2);
        }
        if (devisRequest.getAssurance() != null) {
            existingDevis.setAssurance(devisRequest.getAssurance());
        }
        if (devisRequest.getTechnicien() != null) {
            Technicien technicien = techRepo.findById(devisRequest.getTechnicien().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Technicien non trouvé avec l'ID : " + devisRequest.getTechnicien().getId()));
            existingDevis.setTechnicien(technicien);
        }


        return devis_repo.save(existingDevis);
    }
public void deleteDevis(Long id) {
        if (!devis_repo.existsById(id)) {
            throw new EntityNotFoundException("Devis non trouvé avec l'ID : " + id);
        }
        devis_repo.deleteById(id);
    }

}
