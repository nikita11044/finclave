package equinox.account.jpa;

import equinox.account.model.entity.NotificationOutboxEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationOutboxRepository extends JpaRepository<NotificationOutboxEntry, Long> {
    List<NotificationOutboxEntry> findAllByDeliveredFalse();

    @Modifying
    @Query("update NotificationOutboxEntry n set n.delivered = true where n.id = :id")
    void markDelivered(@Param("id") UUID id);
}
