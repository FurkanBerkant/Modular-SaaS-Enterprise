package pro.turkninja.saas.provider;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record ProviderOnboardingRequest(
        @NotBlank(message = "İşletme adı zorunludur") String businessName,
        @NotBlank(message = "Kategori zorunludur") String category,
        String city,
        String fullAddress,
        Map<String, Object> customAttributes
) {}