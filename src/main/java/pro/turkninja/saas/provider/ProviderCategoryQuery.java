package pro.turkninja.saas.provider;

import java.util.Optional;

public interface ProviderCategoryQuery {
    Optional<String> findCategoryByProviderId(String providerId);
}