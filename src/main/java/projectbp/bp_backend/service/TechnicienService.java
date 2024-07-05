package projectbp.bp_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Technicien;
import projectbp.bp_backend.dao.TechnicienRepo;
import projectbp.bp_backend.dto.CRUD.TechnicienRequest;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TechnicienService {

    public Optional<Technicien> findByMatricule(String matricule) {
        return techRepo.findByMatricule(matricule);
    }

    public List<Technicien> findAll() {
        return techRepo.findAll();
    }

    public Technicien saveTechnicien(TechnicienRequest technicienRequest) {
        Technicien technicien = Technicien.builder()
                .nom(technicienRequest.getNom())
                .prenom(technicienRequest.getPrenom())
                .specialite(technicienRequest.getSpecialite())
                .matricule(technicienRequest.getMatricule())
                .build();
        return techRepo.save(technicien);
    }
    public Technicien findById(Long id) {
        return techRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Technicien non trouvé avec l'ID : " + id));
    }
    public Technicien updateTechnicien(Long id, TechnicienRequest technicienRequest) {
        Technicien existingTechnicien = findById(id);
        existingTechnicien.setNom(technicienRequest.getNom());
        existingTechnicien.setPrenom(technicienRequest.getPrenom());
        existingTechnicien.setSpecialite(technicienRequest.getSpecialite());
        existingTechnicien.setMatricule(technicienRequest.getMatricule());
        return techRepo.save(existingTechnicien);
    }
    public void deleteTechnicien(Long id) {
        Technicien technicienToDelete = techRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Technicien non trouvé avec l'ID : " + id));

        techRepo.delete(technicienToDelete);
    }

    private final TechnicienRepo techRepo;
}
