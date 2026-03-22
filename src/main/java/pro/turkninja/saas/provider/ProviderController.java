package pro.turkninja.saas.provider;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping("/catalog/add")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<Map<String, String>> addServiceItem(
            @RequestParam @NotBlank String name,
            @RequestParam @Positive int durationMinutes,
            @RequestParam @Positive BigDecimal price) {

        providerService.addServiceItem(name, durationMinutes, price);
        return ResponseEntity.ok(Map.of("message", "Hizmet başarıyla eklendi."));
    }

    @DeleteMapping("/danger-zone/delete-all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteAllProviders() {
        providerService.deleteAllProviders();
        return ResponseEntity.ok(Map.of("message", "Tüm sağlayıcılar silindi."));
    }
}