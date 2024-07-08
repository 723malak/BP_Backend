package projectbp.bp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Demande;
import projectbp.bp_backend.bean.Facture;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.DemandeRepo;
import projectbp.bp_backend.dao.FactureRepo;
import projectbp.bp_backend.dao.UserRepo;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DemandeService {

    private final DemandeRepo demandeRepo;
    private final AuthenticationUserService authenticationUserService;
    private final UserRepo userRepo;
    private final FactureRepo factureRepo;


    public List<Demande> getDemandesBySender(String senderName) {
        Optional<User> userOptional = userRepo.findByEmail(senderName);
        if (userOptional.isPresent()) {
            User sender = userOptional.get();
            if ("USER".equals(sender.getRole())) {
                return demandeRepo.findBySender(sender);
            } else {
                throw new EntityNotFoundException("Utilisateur avec le nom " + senderName + " n'est pas de rôle USER.");
            }
        } else {
            throw new EntityNotFoundException("Utilisateur avec le nom " + senderName + " non trouvé.");
        }
    }

    public List<Demande> getAllDemandes() {
        return demandeRepo.findAll();
    }

    public Demande createDemandeFacture(Demande demande, String factureNumero, String errorMessage) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("Current user is not authenticated.");
        }
        User currentUser = (User) authentication.getPrincipal();
        if (!currentUser.getRole().equals("USER")) {
            throw new AccessDeniedException("Only users with role USER can create a demande.");
        }
        demande.setSender(currentUser);
        demande.setCreatedDate(new Date());
        demande.setReceiver(findSupervisor());
        if (factureNumero != null) {
            Optional<Facture> optionalFacture = factureRepo.findByNumero(factureNumero);
            if (optionalFacture.isPresent()) {
                demande.setFacture(optionalFacture.get());
            } else {
                throw new EntityNotFoundException("Facture with numero " + factureNumero + " not found.");
            }
        }
        if (errorMessage != null) {
            demande.setErrorMessage(errorMessage);
        }
        return demandeRepo.save(demande);
    }


    public void approveDemande(Long demandeId) {
        Demande demande = demandeRepo.findById(demandeId)
                .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée avec l'ID : " + demandeId));
        demande.setReceiver(authenticationUserService.getCurrentUser());
        demandeRepo.save(demande);
    }

    public User findSupervisor() {
        return userRepo.findByRole("SUPERVISOR")
                .orElseThrow(() -> new RuntimeException("Superviseur non trouvé."));
    }
}
