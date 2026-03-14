package pro.turkninja.saas.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.turkninja.saas.security.AppUser;
import pro.turkninja.saas.security.UserRepository;
import pro.turkninja.saas.security.UserRole;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    @Transactional
    public void completeOnboarding(ProviderOnboardingRequest request) {
        // Şu an giriş yapmış olan kullanıcının e-postasını Spring Security'den alıyoruz
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = userRepository.findByEmail(currentUserEmail).orElseThrow();

        // 1. Kullanıcının rolünü terfi ettir (Artık sıradan bir müşteri değil, patron!)
        user.setRole(UserRole.PROVIDER);
        userRepository.save(user);

        Provider newProvider = new Provider();
        newProvider.setId(user.getId());
        newProvider.setBusinessName(request.businessName());
        newProvider.setCategory(request.category());
        newProvider.setAddress(new Provider.Address(request.city(), null, request.fullAddress()));
        newProvider.setCustomAttributes(request.customAttributes());
        newProvider.setActive(true);

        providerRepository.save(newProvider);
    }
}
