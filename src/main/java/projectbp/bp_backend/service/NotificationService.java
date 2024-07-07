package projectbp.bp_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.bean.Notification;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dao.DevisRepo;
import projectbp.bp_backend.dao.FactureRepo;
import projectbp.bp_backend.dao.NotificationRepo;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationService {

    @Autowired
    private DevisRepo devisRepo;

    @Autowired
    private FactureRepo factureRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    /*
    public void checkAndCreateNotifications() {
        List<Devis> devisList = devisRepo.findAll();
        Date currentDate = new Date();

        for (Devis devis : devisList) {
            long diffInMillies = Math.abs(currentDate.getTime() - devis.getDate().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diff >= 30 && !factureRepo.existsByDevis(devis)) {
                createNotification(devis);
            }
        }
    }
    */

    public void checkAndCreateNotifications() {
        List<Devis> devisList = devisRepo.findAll();
        Date currentDate = new Date();

        for (Devis devis : devisList) {
            long diffInMillies = Math.abs(currentDate.getTime() - devis.getDate().getTime());
            long diffInSeconds = diffInMillies / (1000 * 60 * 60);

            if (diffInSeconds >= 10 && !factureRepo.existsByDevis(devis)) {
                createNotification(devis);
            }
        }
    }


    private void createNotification(Devis devis) {
        User user = devis.getTraitepar();
        String message = "Devis " + devis.getNumero() + " has no associated facture for over 30 days.";

        Notification notification = Notification.builder()
                .message(message)
                .user(user)
                .build();

        notificationRepo.save(notification);
    }

    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepo.findByUser(user);
    }
}
