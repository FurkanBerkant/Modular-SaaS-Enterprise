package pro.turkninja.saas.tenant;


import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;
import pro.turkninja.saas.appointment.Appointment;

@Component
public class TenantInjectionCallback implements BeforeConvertCallback<Appointment> {

    @Override
    public Appointment onBeforeConvert(Appointment entity, String collection) {
        String currentTenantId = TenantContext.getTenantId();

        if (currentTenantId != null && entity.getProviderId() == null) {
            entity.setProviderId(currentTenantId);
        }

        return entity;
    }
}