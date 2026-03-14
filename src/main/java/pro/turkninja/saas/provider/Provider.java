package pro.turkninja.saas.provider;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
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
    private Storefront storefront = new Storefront("Merhaba, hizmetlerime hoş geldiniz.", "#3b82f6", null, null);

    private Instant createdAt = Instant.now();
    public record Address(String city, String district, String fullAddress) {}

}
