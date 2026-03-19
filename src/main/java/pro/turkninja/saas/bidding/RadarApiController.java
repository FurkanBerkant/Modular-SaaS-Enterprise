package pro.turkninja.saas.bidding;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.turkninja.saas.provider.Provider;
import pro.turkninja.saas.provider.ProviderRepository;
import pro.turkninja.saas.tenant.TenantContext;

import java.util.List;

    @RestController
    @RequestMapping("/api/radar")
    @RequiredArgsConstructor
    public class RadarApiController {

        private final ServiceRequestRepository requestRepository;
        private final ProviderRepository providerRepository;

        @GetMapping("/active-requests")
        @PreAuthorize("hasRole('PROVIDER')") // Sadece işletmeler girebilir!
        public List<ServiceRequest> getRadarRequests() {

            // 1. Giren işletmenin ID'sini TenantContext'ten alıyoruz
            String providerId = TenantContext.getTenantId();
            Provider provider = providerRepository.findById(providerId).orElseThrow();

            // 2. İşletmenin kendi kategorisindeki (Örn: SPOR_HOCASI) AÇIK ilanları bulup döndürüyoruz
            return requestRepository.findByCategoryAndStatusOrderByCreatedAtDesc(
                    provider.getCategory(),
                    RequestStatus.OPEN
            );
        }
    }
