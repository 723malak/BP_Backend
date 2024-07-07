package projectbp.bp_backend.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import projectbp.bp_backend.service.NotificationService;

@Component
public class ScheduledTasks {

    @Autowired
    private NotificationService notificationService;

    /*@Scheduled(cron = "0 0 0 * * ?") */ // Run once a day at midnight
    @Scheduled(fixedRate = 1000)
    public void checkDevisAndCreateNotifications() {
        notificationService.checkAndCreateNotifications();
    }
}
