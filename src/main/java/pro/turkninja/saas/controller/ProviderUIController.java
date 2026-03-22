package pro.turkninja.saas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.turkninja.saas.appointment.Appointment;
import pro.turkninja.saas.appointment.AppointmentRepository;
import pro.turkninja.saas.appointment.AppointmentStatus;
import pro.turkninja.saas.provider.*;
import pro.turkninja.saas.tenant.TenantContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProviderUIController {

    private final ProviderService providerService;
    private final StorefrontService storefrontService;
    private final AppointmentRepository appointmentRepository;
    private final CrmService crmService;

    // ── Dashboard ──────────────────────────────────────────

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PROVIDER')")
    public String dashboard(Model model) {
        Provider provider = providerService.getCurrentProvider();
        model.addAttribute("provider", provider);
        List<Appointment> pending = appointmentRepository
                .findByProviderIdAndStatusOrderByDateAscTimeAsc(
                        TenantContext.getTenantId(), AppointmentStatus.PENDING);
        model.addAttribute("pendingAppointments", pending);
        return "provider/dashboard";
    }

    // ── Onboarding ─────────────────────────────────────────

    @GetMapping("/provider/onboarding")
    public String onboarding() {
        return "provider/onboarding";
    }

    @PostMapping("/provider/onboarding")
    public String handleOnboarding(
            @RequestParam String businessName,
            @RequestParam String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String fullAddress,
            Model model) {
        try {
            providerService.completeOnboarding(
                    new ProviderOnboardingRequest(businessName, category, city, fullAddress, null)
            );
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Bir hata oluştu: " + e.getMessage());
            return "provider/onboarding";
        }
    }

    // ── Catalog ────────────────────────────────────────────

    @GetMapping("/provider/catalog")
    @PreAuthorize("hasRole('PROVIDER')")
    public String catalog(Model model) {
        model.addAttribute("provider", providerService.getCurrentProvider());
        return "provider/catalog";
    }

    @PostMapping("/provider/catalog/add")
    @PreAuthorize("hasRole('PROVIDER')")
    public String addService(
            @RequestParam String name,
            @RequestParam int durationMinutes,
            @RequestParam BigDecimal price) {
        providerService.addServiceItem(name, durationMinutes, price);
        return "redirect:/provider/catalog";
    }

    @PostMapping("/provider/catalog/delete/{serviceId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public String deleteService(@PathVariable String serviceId) {
        providerService.removeServiceItem(serviceId);
        return "redirect:/provider/catalog";
    }

    // ── Storefront ─────────────────────────────────────────

    @GetMapping("/provider/storefront-settings")
    @PreAuthorize("hasRole('PROVIDER')")
    public String storefrontSettings(Model model) {
        Provider provider = providerService.getCurrentProvider();
        model.addAttribute("provider", provider);
        return "provider/storefront-settings";
    }

    @PostMapping("/provider/storefront")
    @PreAuthorize("hasRole('PROVIDER')")
    public String updateStorefront(
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String themeColor,
            @RequestParam(required = false) MultipartFile profileImage,
            Model model) {
        try {
            storefrontService.updateStorefront(bio, themeColor, profileImage);
            return "redirect:/provider/storefront-settings?saved=true";
        } catch (IOException e) {
            model.addAttribute("error", "Fotoğraf yüklenemedi.");
            model.addAttribute("provider", providerService.getCurrentProvider());
            return "provider/storefront-settings";
        }
    }

    @GetMapping("/provider/storefront-view")
    @PreAuthorize("hasRole('PROVIDER')")
    public String storefrontView(Model model) {
        model.addAttribute("provider", providerService.getCurrentProvider());
        return "provider/storefront-view";
    }

    // ── Radar ──────────────────────────────────────────────

    @GetMapping("/provider/radar")
    @PreAuthorize("hasRole('PROVIDER')")
    public String radar(Model model) {
        model.addAttribute("provider", providerService.getCurrentProvider());
        return "provider/radar";
    }

    // ── Paket ──────────────────────────────────────────────

    @GetMapping("/provider/paket-ekle")
    @PreAuthorize("hasRole('PROVIDER')")
    public String paketEkle() {
        return "provider/paket-ekle";
    }

    // ── CRM ────────────────────────────────────────────────

    @GetMapping("/provider/crm/{customerId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public String crmProfile(@PathVariable String customerId, Model model) {
        model.addAttribute("customerId", customerId);
        // crm-profile.html ${profile.record()} ve ${profile.history()} kullanıyor
        CustomerCrmProfile profile = crmService.getCustomerProfile(customerId);
        model.addAttribute("profile", profile);
        return "provider/crm-profile";
    }
}