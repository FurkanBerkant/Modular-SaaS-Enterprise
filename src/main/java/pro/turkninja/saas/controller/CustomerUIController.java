package pro.turkninja.saas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pro.turkninja.saas.provider.Provider;
import pro.turkninja.saas.provider.ProviderRepository;
import pro.turkninja.saas.security.AppUser;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CustomerUIController {

    private final ProviderRepository providerRepository;

    /**
     * Booking widget sayfası.
     *
     * providerId query param ile belirli bir provider seçilebilir:
     *   /booking?providerId=abc123
     *
     * providerId verilmezse ilk aktif provider kullanılır (demo amaçlı).
     * Gerçek projede müşteri belirli bir provider'ın sayfasından gelir.
     */
    @GetMapping("/booking")
    public String bookingWidget(
            @RequestParam(required = false) String providerId,
            @AuthenticationPrincipal AppUser currentUser,
            Model model) {

        Optional<Provider> providerOpt = (providerId != null && !providerId.isBlank())
                ? providerRepository.findById(providerId)
                : providerRepository.findAll().stream().filter(Provider::isActive).findFirst();

        providerOpt.ifPresent(p -> model.addAttribute("provider", p));

        return "customer/booking-widget";
    }

    @GetMapping("/request")
    public String newRequest() {
        return "customer/new-request";
    }
}