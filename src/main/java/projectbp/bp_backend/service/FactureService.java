package projectbp.bp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Facture;
import projectbp.bp_backend.bean.Technicien;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dao.FactureRepo;
import projectbp.bp_backend.dto.CRUD.FactureRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FactureService {


    private final DevisRepo devisRepo;

    public Optional<Facture> findByNumero(String numero) {
        return facture_repo.findByNumero(numero);
    }

    public List<Facture> findAll() {
        return facture_repo.findAll();
    }

    public ResponseEntity<Object> createFacture(FactureRequest factureRequest) {
        User user = authenticationUserService.getCurrentUser();
        Optional<Facture> existingFacture = facture_repo.findByNumero(factureRequest.getNumero());
        if (existingFacture.isPresent()) {
            return ResponseEntity.badRequest().body("Facture with this Numero already exists");
        }
        Optional<Devis> existingDevis = devisRepo.findByNumero(factureRequest.getDevis().getNumero());
        if (!existingDevis.isPresent()) {
            return ResponseEntity.badRequest().body("Devis not found");
        }
        Facture facture = new Facture();
        facture.setNumero(factureRequest.getNumero());
        facture.setDate_traitement(new Date());
        facture.setDate_facture(factureRequest.getDate_facture());
        facture.setMontant(factureRequest.getMontant() * 1.2);
        facture.setDevis(existingDevis.get());
        facture.setTraitepar(user);
        Facture savedFacture = facture_repo.save(facture);
        return ResponseEntity.ok(savedFacture);
    }

    public Facture updateFacture(Long id, FactureRequest factureRequest) {
        Facture existingFacture = facture_repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Facture non trouvé avec l'ID : " + id));

        if (factureRequest.getNumero() != null) {
            existingFacture.setNumero(factureRequest.getNumero());
        }
        if(factureRequest.getDate_facture() != null) {
            existingFacture.setDate_facture(factureRequest.getDate_facture());
        }
        if(factureRequest.getMontant() != null) {
            existingFacture.setMontant(factureRequest.getMontant() * 1.2);
        }
        if(factureRequest.getDevis() != null) {
            existingFacture.setDevis(factureRequest.getDevis());
        }
        return facture_repo.save(existingFacture);
    }

    public void deleteFacture(Long id) {
        if (!facture_repo.existsById(id)) {
            throw new EntityNotFoundException("Facture non trouvé avec l'ID : " + id);
        }
        facture_repo.deleteById(id);
    }

    private final AuthenticationUserService authenticationUserService;
    private final FactureRepo facture_repo;

}
