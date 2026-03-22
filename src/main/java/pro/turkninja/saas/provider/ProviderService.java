package pro.turkninja.saas.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.turkninja.saas.security.AppUser;
import pro.turkninja.saas.security.UserRepository;
import pro.turkninja.saas.security.UserRole;
import pro.turkninja.saas.tenant.TenantContext;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProviderService implements ProviderCategoryQuery {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<String> findCategoryByProviderId(String providerId) {
        return providerRepository.findById(providerId)
                .map(Provider::getCategory);
    }

    public void addServiceItem(String name, int duration, BigDecimal price) {
        String providerId = TenantContext.getTenantId();
        Provider provider = providerRepository.findById(providerId).orElseThrow();
        provider.getServices().add(
                new ServiceItem(UUID.randomUUID().toString(), name, duration, price));
        providerRepository.save(provider);
    }

    public void removeServiceItem(String serviceId) {
        String providerId = TenantContext.getTenantId();
        Provider provider = providerRepository.findById(providerId).orElseThrow();
        provider.getServices().removeIf(s -> s.id().equals(serviceId));
        providerRepository.save(provider);
    }

    @Transactional
    public void completeOnboarding(ProviderOnboardingRequest request) {
        String currentUserEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        AppUser user = userRepository.findByEmail(currentUserEmail).orElseThrow();

        user.setRole(UserRole.PROVIDER);
        userRepository.save(user);

        Provider newProvider = new Provider();
        newProvider.setId(user.getId());
        newProvider.setBusinessName(request.businessName());
        newProvider.setCategory(request.category());
        newProvider.setAddress(new Address(request.city(), null, request.fullAddress()));
        if (request.customAttributes() != null) {
            newProvider.setCustomAttributes(request.customAttributes());
        }
        newProvider.setActive(true);
        providerRepository.save(newProvider);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );
    }

    @Transactional
    public void addEmployee(String employeeEmail) {
        String providerId = TenantContext.getTenantId();

        AppUser employee = userRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bu e-posta ile kayıtlı kullanıcı bulunamadı: " + employeeEmail));

        employee.setRole(UserRole.EMPLOYEE);
        employee.setProviderId(providerId); // ✅ işte burada set ediliyor
        userRepository.save(employee);
    }

    public Provider getCurrentProvider() {
        String providerId = TenantContext.getTenantId();
        return providerRepository.findById(providerId).orElseThrow();
    }

    public void deleteAllProviders() {
        providerRepository.deleteAll();
    }
}