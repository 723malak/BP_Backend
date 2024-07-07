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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String message;
    private Date notificationDate;

    @ManyToOne
    private User user;

    @PrePersist
    public void setNotificationDate() {
        this.notificationDate = new Date();
    }
}
