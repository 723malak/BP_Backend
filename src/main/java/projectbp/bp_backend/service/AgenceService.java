package projectbp.bp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.AgenceRepo;
import projectbp.bp_backend.dto.CRUD.AgenceRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgenceService {


    public Optional<Agence> findByNom(String numero) {
        return agence_repo.findByNom(numero);
    }

    public List<Agence> findAll() {
        return agence_repo.findAll();
    }

    public ResponseEntity<Object> createAgence(AgenceRequest agenceRequest) {
        Optional<Agence> existingAgence = agence_repo.findByNom(agenceRequest.getNom());
        if (existingAgence.isPresent()) {
            return null;
        }
        Agence agence = new Agence();
        agence.setNom(agenceRequest.getNom());
        return ResponseEntity.ok(agence_repo.save(agence));
    }


    public Agence updateAgence(Long id, AgenceRequest agenceRequest) {
        Agence existingAgence = agence_repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agence non trouvée avec l'ID : " + id));

        if (existingAgence.getNom() != null) {
            existingAgence.setNom(agenceRequest.getNom());
        }


        return agence_repo.save(existingAgence);
    }


    public void deleteAgence(Long id) {
        if (!agence_repo.existsById(id)) {
            throw new EntityNotFoundException("Agence non trouvé avec l'ID : " + id);
        }
        agence_repo.deleteById(id);
    }
    private final AgenceRepo agence_repo;

}
