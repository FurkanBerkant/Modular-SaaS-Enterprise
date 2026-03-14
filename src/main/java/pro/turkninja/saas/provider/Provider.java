package pro.turkninja.saas.provider;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "providers")
public class Provider {
    @Id
    private String id;

    private String businessName;
    private String category;
    private boolean isActive;
    private Instant createdAt = Instant.now();
}
