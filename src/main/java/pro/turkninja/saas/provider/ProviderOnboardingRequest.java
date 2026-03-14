package pro.turkninja.saas.provider;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

// Formdan gelecek tüm verileri taşıyan nesne
public record ProviderOnboardingRequest(
        @NotBlank(message = "İşletme adı zorunludur") String businessName,
        @NotBlank(message = "Kategori zorunludur") String category,
        String city,
        String fullAddress,
        Map<String, Object> customAttributes // Mesleğe özel esnek alanlar
) {}