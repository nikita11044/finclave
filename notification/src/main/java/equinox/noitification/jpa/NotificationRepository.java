package equinox.noitification.jpa;

import equinox.noitification.model.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query(value = """
    INSERT INTO notifications (notification_id, login, message)
    VALUES (:notificationId, :login, :message)
    ON CONFLICT (notification_id) DO NOTHING
    """, nativeQuery = true)
    void insert(
            @Param("notificationId") UUID notificationId,
            @Param("login") String login,
            @Param("message") String message
    );


    Page<Notification> findAllByLogin(String login, Pageable pageable);
}
