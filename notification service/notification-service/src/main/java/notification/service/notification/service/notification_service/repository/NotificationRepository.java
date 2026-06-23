package notification.service.notification.service.notification_service.repository;

import notification.service.notification.service.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
