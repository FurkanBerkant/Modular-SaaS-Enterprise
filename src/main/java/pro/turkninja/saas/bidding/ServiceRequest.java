package pro.turkninja.saas.bidding;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Document(collection = "service_requests")
@Data
public class ServiceRequest {

    @Id
    private String id;
    private String customerId;
    private String category;
    private String title;
    private String description;
    private BigDecimal maxBudget;
    private LocalDate deadline;
    private RequestStatus status = RequestStatus.OPEN;
    private Instant createdAt = Instant.now();

    public void cancel() {
        if (this.status != RequestStatus.OPEN) {
            throw new IllegalStateException("Sadece açık olan ilanlar iptal edilebilir.");
        }
        this.status = RequestStatus.CANCELLED;
    }
}
