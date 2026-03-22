package pro.turkninja.saas.bidding;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pro.turkninja.saas.security.AppUser;

import java.util.Map;

/**
 * Hizmet talebi oluşturma API'si.
 * new-request.html bu endpoint'i CSRF token ile çağırır.
 */
@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestApiController {

    private final ServiceRequestRepository requestRepository;

    /**
     * Yeni hizmet talebi yayınlar.
     * customerId oturumdaki kullanıcıdan alınır — frontend'den güvenilmez.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Map<String, String>> createRequest(
            @RequestBody ServiceRequest payload,
            @AuthenticationPrincipal AppUser currentUser) {

        payload.setCustomerId(currentUser.getId());
        payload.setStatus(RequestStatus.OPEN);
        requestRepository.save(payload);

        return ResponseEntity.ok(Map.of(
                "message", "İlanınız başarıyla yayına alındı! İşletmelerden teklif bekleyebilirsiniz."));
    }
}