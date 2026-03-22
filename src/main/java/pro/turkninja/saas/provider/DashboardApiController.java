package pro.turkninja.saas.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.turkninja.saas.appointment.AppointmentQueryService;
import pro.turkninja.saas.appointment.AppointmentStatus;
import pro.turkninja.saas.appointment.AppointmentSummary;
import pro.turkninja.saas.tenant.TenantContext;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardApiController {

    private final AppointmentQueryService appointmentQueryService;
    private final ProviderRepository providerRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('PROVIDER')")
    public ProviderDashboardStats getProviderStats() {
        String providerId = TenantContext.getTenantId();

        Map<AppointmentStatus, Integer> counts =
                appointmentQueryService.countByStatus(providerId);

        int total     = counts.values().stream().mapToInt(Integer::intValue).sum();
        int completed = counts.getOrDefault(AppointmentStatus.COMPLETED, 0);
        int cancelled = counts.getOrDefault(AppointmentStatus.REJECTED,  0);
        int pending   = counts.getOrDefault(AppointmentStatus.PENDING,   0);

        String thisMonth = YearMonth.now().toString();
        Provider provider = providerRepository.findById(providerId).orElseThrow();

        double revenue = appointmentQueryService
                .findByProviderStatusAndMonth(providerId, AppointmentStatus.APPROVED, thisMonth)
                .stream()
                .mapToDouble(appt -> provider.getServices().stream()
                        .filter(s -> s.id().equals(appt.serviceId()))
                        .mapToDouble(s -> s.price().doubleValue())
                        .findFirst().orElse(0.0))
                .sum();

        return new ProviderDashboardStats(total, completed, cancelled, pending, revenue);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('PROVIDER')")
    public List<AppointmentSummary> getPendingAppointments() {
        String providerId = TenantContext.getTenantId();
        return appointmentQueryService.findByProviderAndStatus(
                providerId, AppointmentStatus.PENDING);
    }
}