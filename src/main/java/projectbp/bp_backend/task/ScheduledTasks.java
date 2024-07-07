package projectbp.bp_backend.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import projectbp.bp_backend.service.NotificationService;

import java.util.Date;

@Component
public class ScheduledTasks {

    @Autowired
    private NotificationService notificationService;


    @Scheduled(fixedRate = 5000)
    public void checkDevisAndCreateNotifications() {
        System.out.println("Running scheduled task at " + new Date());
        notificationService.checkAndCreateNotifications();
    }
}
/*@Scheduled(cron = "0 0 0 * * ?") */