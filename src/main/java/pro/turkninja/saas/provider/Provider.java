package pro.turkninja.saas.provider;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "providers")
public class Provider {
    @Id
    private String id;
    private Address address;

    private String businessName;
    private String category;
    private boolean isActive;
    private Map<String, Object> customAttributes = new HashMap<>();
    private Storefront storefront = new Storefront("Merhaba, hizmetlerime hoş geldiniz.", "#3b82f6",
            null, null);
    private List<ServiceItem> services = new ArrayList<>();
    private List<DailySchedule> weeklySchedule = new ArrayList<>();
    private Instant createdAt = Instant.now();
}
