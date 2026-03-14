package pro.turkninja.saas.tenant;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantAwareAspect {

    @Before("execution(* pro.turkninja.saas.*.repository.*.*(..))")
    public void addTenantFilter(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String currentTenantId = TenantContext.getTenantId();

        if (currentTenantId == null) return;

        for (Object arg : args) {
            if (arg instanceof org.springframework.data.mongodb.core.query.Query query) {
                // Sorguya providerId şartını sessizce enjekte ediyoruz
                query.addCriteria(Criteria.where("providerId").is(currentTenantId));
            }
        }
    }
}