package equinox.account.jpa;

import equinox.account.model.entity.NotificationOutboxEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationOutboxRepository extends JpaRepository<NotificationOutboxEntry, Long> {
    List<NotificationOutboxEntry> findAllByDeliveredFalse();
}
