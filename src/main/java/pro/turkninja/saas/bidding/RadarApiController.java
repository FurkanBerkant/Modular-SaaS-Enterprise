package pro.turkninja.saas.bidding;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.turkninja.saas.provider.ProviderCategoryQuery;
import pro.turkninja.saas.tenant.TenantContext;

import java.util.List;

/**
 * Radar API — provider'ın kategorisine uygun açık ilanları döner.
 *
 * provider modülüne bağımlılık ProviderCategoryQuery (public API) üzerinden —
 * ProviderRepository'ye doğrudan erişim YOK.
 */
@RestController
@RequestMapping("/api/radar")
@RequiredArgsConstructor
public class RadarApiController {

    private final ServiceRequestRepository requestRepository;
    private final ProviderCategoryQuery providerCategoryQuery;

    @GetMapping("/active-requests")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<?> getRadarRequests() {
        String providerId = TenantContext.getTenantId();

        String category = providerCategoryQuery
                .findCategoryByProviderId(providerId)
                .orElseThrow(() -> new IllegalStateException(
                        "Provider profili bulunamadı. Önce onboarding tamamlanmalı."));

        List<ServiceRequest> requests = requestRepository
                .findByCategoryAndStatusOrderByCreatedAtDesc(category, RequestStatus.OPEN);

        return ResponseEntity.ok(requests);
    }
}