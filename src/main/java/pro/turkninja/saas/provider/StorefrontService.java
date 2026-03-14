package pro.turkninja.saas.provider;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import pro.turkninja.saas.tenant.TenantContext;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class StorefrontService {

    private final ProviderRepository providerRepository;
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    public StorefrontService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
        new File(uploadDir).mkdirs();
    }

    public void updateStorefront(String bio, String themeColor, MultipartFile profileImage) throws IOException {
        String providerId = TenantContext.getTenantId();
        Provider provider = providerRepository.findById(providerId).orElseThrow();

        String imageUrl = provider.getStorefront().profileImageUrl();

        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            profileImage.transferTo(new File(uploadDir + fileName));
            imageUrl = "/uploads/" + fileName;
        }

        Storefront updatedStorefront = new Storefront(bio, themeColor, provider.getStorefront().bannerImageUrl(), imageUrl);
        provider.setStorefront(updatedStorefront);

        providerRepository.save(provider);
    }
}
