package pro.turkninja.saas.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * CRM API — müşteri profili ve not yönetimi.
 * crm-profile.html bu controller'ı kullanır.
 */
@RestController
@RequestMapping("/api/crm")
@RequiredArgsConstructor
public class CrmApiController {

    private final CrmService crmService;

    /** Müşteri CRM profilini döner (randevu geçmişi + notlar). */
    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public CustomerCrmProfile getProfile(@PathVariable String customerId) {
        return crmService.getCustomerProfile(customerId);
    }

    /**
     * Müşteriye gizli not ekler.
     * crm-profile.html'de CSRF token data-attribute'tan alınıp header'a ekleniyor.
     */
    @PostMapping("/{customerId}/notes")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<Map<String, String>> addNote(
            @PathVariable String customerId,
            @RequestBody NoteRequest request) {

        try {
            crmService.addPrivateNoteToCustomer(customerId, request.content());
            return ResponseEntity.ok(Map.of("message", "Not kaydedildi."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── Request DTO ───────────────────────────────────────────────────────────
    record NoteRequest(String content) {}
}