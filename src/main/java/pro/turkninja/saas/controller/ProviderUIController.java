package pro.turkninja.saas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pro.turkninja.saas.provider.ProviderService;

@Controller
@RequiredArgsConstructor
public class ProviderUIController {

    private final ProviderService providerService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PROVIDER')")
    public String dashboard(Model model) {
        model.addAttribute("provider", providerService.getCurrentProvider());
        return "provider/radar";
    }

    @GetMapping("/provider/catalog")
    @PreAuthorize("hasRole('PROVIDER')")
    public String catalog(Model model) {
        model.addAttribute("provider", providerService.getCurrentProvider());
        return "provider/catalog";
    }

    @GetMapping("/provider/onboarding")
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
    public String radar(Model model) {
        model.addAttribute("provider", providerService.getCurrentProvider());
        return "provider/radar";
    }

    @GetMapping("/provider/storefront-settings")
    @PreAuthorize("hasRole('PROVIDER')")
    public String storefrontSettings(Model model) {
        model.addAttribute("provider", providerService.getCurrentProvider());
        return "provider/storefront-settings";
    }

    @GetMapping("/provider/storefront-view")
    @PreAuthorize("hasRole('PROVIDER')")
    public String storefrontView(Model model) {
        model.addAttribute("provider", providerService.getCurrentProvider());
        return "provider/storefront-view";
    }
}