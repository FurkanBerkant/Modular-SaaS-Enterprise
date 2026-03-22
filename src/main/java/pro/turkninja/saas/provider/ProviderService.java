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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    public void addServiceItem(String name, int duration, BigDecimal price) {
        String providerId = TenantContext.getTenantId();
        Provider provider = providerRepository.findById(providerId).orElseThrow();

        ServiceItem newItem = new ServiceItem(
                UUID.randomUUID().toString(),
                name,
                duration,
                price
        );

        provider.getServices().add(newItem);
        providerRepository.save(provider);
    }

    @Transactional
    public void completeOnboarding(ProviderOnboardingRequest request) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = userRepository.findByEmail(currentUserEmail).orElseThrow();

        user.setRole(UserRole.PROVIDER);
        userRepository.save(user);

        Provider newProvider = new Provider();
        newProvider.setId(user.getId());
        newProvider.setBusinessName(request.businessName());
        newProvider.setCategory(request.category());
        newProvider.setAddress(new Address(request.city(), null, request.fullAddress()));
        newProvider.setCustomAttributes(request.customAttributes());
        newProvider.setActive(true);

        providerRepository.save(newProvider);
    }

    public Provider getCurrentProvider() {
        String providerId = TenantContext.getTenantId();
        return providerRepository.findById(providerId).orElseThrow();
    }

    public void deleteAllProviders() {
        providerRepository.deleteAll();
    }
}