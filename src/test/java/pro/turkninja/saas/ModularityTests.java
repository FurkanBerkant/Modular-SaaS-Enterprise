package pro.turkninja.saas;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModularityTests {

    @Test
    void validateArchitecturalRules() {
        ApplicationModules.of(TurkninjaSaaSApplication.class).verify();
    }
}
