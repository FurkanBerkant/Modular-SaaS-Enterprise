package pro.turkninja.saas;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModularityTests {

    @Test
    void validateArchitecturalRules() {
        // Bu kod tüm projeyi tarar. Eğer bir modül, başka bir modülün
        // gizli/iç (internal) sınıflarına doğrudan erişmişse bu test PATLAR!
        // Böylece spagetti kod yazılmasını build aşamasında engellemiş oluruz.
        ApplicationModules.of(TurkninjaSaaSApplication.class).verify();
    }
}
