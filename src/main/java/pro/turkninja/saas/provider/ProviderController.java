package pro.turkninja.saas.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping("/employees/invite")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<Map<String, String>> inviteEmployee(
            @RequestBody EmployeeInviteRequest request) {
        try {
            providerService.addEmployee(request.email());
            return ResponseEntity.ok(Map.of(
                    "message", request.email() + " çalışan olarak eklendi."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/danger-zone/delete-all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteAllProviders() {
        providerService.deleteAllProviders();
        return ResponseEntity.ok(Map.of("message", "Tüm sağlayıcılar silindi."));
    }

    record EmployeeInviteRequest(String email) {}
}