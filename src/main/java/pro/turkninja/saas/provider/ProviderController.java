package pro.turkninja.saas.provider;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;
    private final StorefrontService storefrontService;

    // ── Katalog ───────────────────────────────────────────────────────────────

    /**
     * Kataloga yeni hizmet ekler.
     * catalog.html form POST → /provider/catalog/add
     */
    @PostMapping("/provider/catalog/add")
    @PreAuthorize("hasRole('PROVIDER')")
    public String addServiceItem(
            @RequestParam @NotBlank String name,
            @RequestParam @Positive int durationMinutes,
            @RequestParam @Positive BigDecimal price) {

        providerService.addServiceItem(name, durationMinutes, price);
        return "redirect:/provider/catalog";
    }

    /**
     * Katalogdan hizmet siler.
     * catalog.html form POST → /provider/catalog/delete/{id}
     */
    @PostMapping("/provider/catalog/delete/{serviceId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public String deleteServiceItem(@PathVariable String serviceId) {
        providerService.removeServiceItem(serviceId);
        return "redirect:/provider/catalog";
    }

    // ── Storefront ────────────────────────────────────────────────────────────

    /**
     * Vitrin ayarlarını günceller.
     * storefront-settings.html form POST → /provider/storefront
     */
    @PostMapping("/provider/storefront")
    @PreAuthorize("hasRole('PROVIDER')")
    public String updateStorefront(
            @RequestParam String bio,
            @RequestParam String themeColor,
            @RequestParam(required = false) MultipartFile profileImage) throws IOException {

        storefrontService.updateStorefront(bio, themeColor, profileImage);
        return "redirect:/provider/storefront-settings";
    }

    // ── Onboarding ────────────────────────────────────────────────────────────

    /**
     * Provider onboarding formunu işler.
     * onboarding.html form POST → /provider/onboarding
     */
    @PostMapping("/provider/onboarding")
    public String completeOnboarding(@ModelAttribute ProviderOnboardingRequest request) {
        providerService.completeOnboarding(request);
        return "redirect:/dashboard";
    }

    // ── REST: Danger zone (SUPER_ADMIN) ──────────────────────────────────────

    @RestController
    @RequestMapping("/api/providers")
    @RequiredArgsConstructor
    static class ProviderRestController {
        private final ProviderService providerService;

        @DeleteMapping("/danger-zone/delete-all")
        @PreAuthorize("hasRole('SUPER_ADMIN')")
        public ResponseEntity<Map<String, String>> deleteAllProviders() {
            providerService.deleteAllProviders();
            return ResponseEntity.ok(Map.of("message", "Tüm sağlayıcılar silindi."));
        }
    }
}