package projectbp.bp_backend.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String numero;
    private Date date;
    private String equipementE;
    private String prestataire;
    private Double montant;
    private Double montantTTC;
    private Double pourcentageTTC;
    private Boolean assurance;

    @ManyToOne
    private Technicien technicien;

    @ManyToOne
    private User traitepar;

    @PrePersist
    public void initTraitepar() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                this.traitepar = (User) principal;
            } else {
                throw new IllegalStateException("Utilisateur non trouvé dans le contexte de sécurité.");
            }
        } else {
            throw new IllegalStateException("Aucun utilisateur authentifié trouvé.");
        }
    }
}
