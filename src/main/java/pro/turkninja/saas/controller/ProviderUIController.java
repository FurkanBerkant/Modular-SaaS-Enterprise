package pro.turkninja.saas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pro.turkninja.saas.appointment.AppointmentQueryService;
import pro.turkninja.saas.appointment.AppointmentStatus;
import pro.turkninja.saas.provider.CrmService;
import pro.turkninja.saas.provider.ProviderService;
import pro.turkninja.saas.security.AppUser;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ProviderUIController {

    private final ProviderService providerService;
    private final AppointmentQueryService appointmentQueryService;
    private final CrmService crmService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PROVIDER')")
    public String dashboard(Model model) {
        var provider = providerService.getCurrentProvider();
        var pending  = appointmentQueryService.findByProviderAndStatus(
                provider.getId(), AppointmentStatus.PENDING);
        model.addAttribute("provider", provider);
        model.addAttribute("pendingAppointments", pending);
        return "provider/dashboard";
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

    @GetMapping("/provider/crm/{customerId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public String crmProfile(@PathVariable String customerId, Model model) {
        model.addAttribute("customerId", customerId);
        model.addAttribute("profile", crmService.getCustomerProfile(customerId));
        return "provider/crm-profile";
    }

    @GetMapping("/employee/dashboard")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'PROVIDER')")
    public String employeeDashboard(
            @AuthenticationPrincipal AppUser currentUser,
            Model model) {
        String today = LocalDate.now().toString();
        var appointments = appointmentQueryService
                .findByEmployeeId(currentUser.getId(), today);
        model.addAttribute("appointments", appointments);
        try {
            model.addAttribute("provider", providerService.getCurrentProvider());
        } catch (Exception ignored) {}
        return "employee/dashboard";
    }
}