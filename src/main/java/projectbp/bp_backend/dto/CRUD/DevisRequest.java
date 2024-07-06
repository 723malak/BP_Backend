package projectbp.bp_backend.dto.CRUD;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.bean.Technicien;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevisRequest {

    private String numero;
    private Date date;
    private String equipementE;
    private String prestataire;
    private Double montant;
    private Boolean assurance;
    private Technicien technicien;
    private Agence agence;


}
