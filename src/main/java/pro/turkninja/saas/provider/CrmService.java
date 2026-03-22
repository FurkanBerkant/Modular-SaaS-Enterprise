package pro.turkninja.saas.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.turkninja.saas.appointment.AppointmentRepository;
import pro.turkninja.saas.appointment.Appointment;
import pro.turkninja.saas.tenant.TenantContext;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrmService {

    private final ClientRecordRepository recordRepository;
    private final AppointmentRepository appointmentRepository;

    public CustomerCrmProfile getCustomerProfile(String customerId) {
        String providerId = TenantContext.getTenantId();

        ClientRecord record = recordRepository.findByProviderIdAndCustomerId(providerId, customerId)
                .orElseGet(() -> {
                    ClientRecord newRecord = new ClientRecord();
                    newRecord.setProviderId(providerId);
                    newRecord.setCustomerId(customerId);
                    return recordRepository.save(newRecord);
                });

        List<Appointment> history = appointmentRepository
                .findByProviderIdAndCustomerIdOrderByDateDesc(providerId, customerId);

        return new CustomerCrmProfile(record, history);
    }

    public void addPrivateNoteToCustomer(String customerId, String noteContent) {
        String providerId = TenantContext.getTenantId();
        ClientRecord record = recordRepository
                .findByProviderIdAndCustomerId(providerId, customerId)
                .orElseThrow();

        record.addNote(noteContent);
        recordRepository.save(record);
    }
}