package pro.turkninja.saas.provider;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    @PostMapping("/paket-ekle")
    @PreAuthorize("hasRole('PROVIDER')")
    public String addPackage() {
        return "Paket başarıyla eklendi!";
    }

    @DeleteMapping("/danger-zone/delete-all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteAllProviders() {
        return "Sistem sıfırlandı.";
    }
}