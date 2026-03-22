package pro.turkninja.saas.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.turkninja.saas.security.AppUser;
import pro.turkninja.saas.security.UserRepository;
import pro.turkninja.saas.security.UserRole;
import pro.turkninja.saas.tenant.TenantContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProviderService implements ProviderCategoryQuery {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    // ── ProviderCategoryQuery (bidding modülüne açık API) ─────────────────────

    @Override
    public Optional<String> findCategoryByProviderId(String providerId) {
        return providerRepository.findById(providerId)
                .map(Provider::getCategory);
    }

    // ── Katalog ───────────────────────────────────────────────────────────────

    public void addServiceItem(String name, int duration, BigDecimal price) {
        String providerId = TenantContext.getTenantId();
        Provider provider = providerRepository.findById(providerId).orElseThrow();

        ServiceItem newItem = new ServiceItem(
                UUID.randomUUID().toString(), name, duration, price);

        provider.getServices().add(newItem);
        providerRepository.save(provider);
    }

    public void removeServiceItem(String serviceId) {
        String providerId = TenantContext.getTenantId();
        Provider provider = providerRepository.findById(providerId).orElseThrow();

        boolean removed = provider.getServices()
                .removeIf(s -> s.id().equals(serviceId));

        if (!removed) {
            throw new IllegalArgumentException("Hizmet bulunamadı: " + serviceId);
        }
        providerRepository.save(provider);
    }

    // ── Onboarding ────────────────────────────────────────────────────────────

    /**
     * Kullanıcıyı PROVIDER rolüne yükseltir ve Provider kaydı oluşturur.
     * Provider.id == AppUser.id olarak bilinçli şekilde eşitlenir —
     * TenantFilter user.getId() ile tenant'ı resolve eder.
     */
    @Transactional
    public void completeOnboarding(ProviderOnboardingRequest request) {
        String currentUserEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        AppUser user = userRepository.findByEmail(currentUserEmail).orElseThrow();

        user.setRole(UserRole.PROVIDER);
        userRepository.save(user);

        Provider newProvider = new Provider();
        newProvider.setId(user.getId());          // Tenant ID = Provider ID = User ID
        newProvider.setBusinessName(request.businessName());
        newProvider.setCategory(request.category());
        newProvider.setAddress(new Address(
                request.city(), null, request.fullAddress()));
        newProvider.setCustomAttributes(
                request.customAttributes() != null ? request.customAttributes() : new java.util.HashMap<>());
        newProvider.setActive(true);

        providerRepository.save(newProvider);
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public Provider getCurrentProvider() {
        String providerId = TenantContext.getTenantId();
        if (providerId == null) {
            throw new IllegalStateException(
                    "TenantContext boş — bu method sadece PROVIDER rolündeki kullanıcılar için çağrılabilir.");
        }
        return providerRepository.findById(providerId).orElseThrow();
    }

    // ── Admin ─────────────────────────────────────────────────────────────────

    public void deleteAllProviders() {
        providerRepository.deleteAll();
    }
}