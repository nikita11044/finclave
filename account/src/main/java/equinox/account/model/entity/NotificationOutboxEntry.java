package equinox.account.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notification_outbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationOutboxEntry {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean delivered = false;
}
