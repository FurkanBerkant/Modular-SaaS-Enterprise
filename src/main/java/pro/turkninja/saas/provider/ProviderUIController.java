package pro.turkninja.saas.provider;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProviderUIController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PROVIDER')")
    public String dashboard() {
        return "provider/radar";
    }

    @GetMapping("/provider/catalog")
    @PreAuthorize("hasRole('PROVIDER')")
    public String catalog() {
        return "provider/catalog";
    }

    @GetMapping("/provider/onboarding")
    @PreAuthorize("hasRole('PROVIDER')")
    public String onboarding() {
        return "provider/onboarding";
    }

    @GetMapping("/provider/paket-ekle")
    @PreAuthorize("hasRole('PROVIDER')")
    public String paketEkle() {
        return "provider/paket-ekle";
    }

    @GetMapping("/provider/radar")
    @PreAuthorize("hasRole('PROVIDER')")
    public String radar() {
        return "provider/radar";
    }

    @GetMapping("/provider/storefront-settings")
    @PreAuthorize("hasRole('PROVIDER')")
    public String storefrontSettings() {
        return "provider/storefront-settings";
    }

    @GetMapping("/provider/storefront-view")
    @PreAuthorize("hasRole('PROVIDER')")
    public String storefrontView() {
        return "provider/storefront-view";
    }
}
