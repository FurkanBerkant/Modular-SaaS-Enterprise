package pro.turkninja.saas.provider;

import java.math.BigDecimal;

public record ServiceItem(
        String id,
        String name,
        int durationMinutes,
        BigDecimal price
) {}
