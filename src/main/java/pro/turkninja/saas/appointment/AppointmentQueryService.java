package pro.turkninja.saas.appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentQueryService {

    private final AppointmentRepository repository;

    public List<AppointmentSummary> findByProviderAndStatus(
            String providerId, AppointmentStatus status) {
        return repository
                .findByProviderIdAndStatusOrderByDateAscTimeAsc(providerId, status)
                .stream().map(this::toSummary).toList();
    }

    public List<AppointmentSummary> findByProviderAndCustomer(
            String providerId, String customerId) {
        return repository
                .findByProviderIdAndCustomerIdOrderByDateDesc(providerId, customerId)
                .stream().map(this::toSummary).toList();
    }

    public List<AppointmentSummary> findByProviderStatusAndMonth(
            String providerId, AppointmentStatus status, String yearMonth) {
        return repository
                .findByProviderIdAndStatusAndDateStartingWith(providerId, status, yearMonth)
                .stream().map(this::toSummary).toList();
    }

    public List<AppointmentSummary> findByEmployeeId(String employeeId, String date) {
        return repository
                .findByEmployeeIdAndDateOrderByTimeAsc(employeeId, date)
                .stream().map(this::toSummary).toList();
    }

    public Map<AppointmentStatus, Integer> countByStatus(String providerId) {
        return repository.countAppointmentsByStatus(providerId)
                .stream()
                .collect(Collectors.toMap(
                        dto -> AppointmentStatus.valueOf(dto.id()),
                        StatusCountDTO::count
                ));
    }

    private AppointmentSummary toSummary(Appointment a) {
        return new AppointmentSummary(
                a.getId(), a.getProviderId(), a.getCustomerId(),
                a.getServiceId(), a.getDate(), a.getTime(), a.getStatus()
        );
    }
}