package projectbp.bp_backend.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String errorMessage;
    private String attachmentUrl;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;



    @ManyToOne
    private Devis devis;

    @ManyToOne
    private Facture facture;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

}
