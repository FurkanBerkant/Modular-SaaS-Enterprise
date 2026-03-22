package pro.turkninja.saas.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.turkninja.saas.appointment.AppointmentRepository;
import pro.turkninja.saas.appointment.AppointmentStatus;
import pro.turkninja.saas.appointment.DashboardStats;
import pro.turkninja.saas.appointment.StatusCountDTO;
import pro.turkninja.saas.tenant.TenantContext;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardApiController {

    private final AppointmentRepository appointmentRepository;
    private final ProviderRepository providerRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('PROVIDER')")
    public DashboardStats getProviderStats() {
        String providerId = TenantContext.getTenantId();

        // Tüm randevuları status'a göre grupla
        List<StatusCountDTO> counts = appointmentRepository.countAppointmentsByStatus(providerId);

        int total = 0, completed = 0, cancelled = 0, pending = 0;

        for (StatusCountDTO dto : counts) {
            total += dto.count();
            switch (dto.id()) {
                case "COMPLETED" -> completed = dto.count();
                case "REJECTED"  -> cancelled = dto.count();
                case "PENDING"   -> pending   = dto.count();
            }
        }

        // Bu ayki onaylı randevulardan beklenen ciro
        String thisMonth = YearMonth.now().toString(); // "2026-03"
        Provider provider = providerRepository.findById(providerId).orElseThrow();

        double revenue = appointmentRepository
                .findByProviderIdAndStatusAndDateStartingWith(providerId, AppointmentStatus.APPROVED, thisMonth)
                .stream()
                .mapToDouble(appt -> provider.getServices().stream()
                        .filter(s -> s.id().equals(appt.getServiceId()))
                        .mapToDouble(s -> s.price().doubleValue())
                        .findFirst()
                        .orElse(0.0))
                .sum();

        return new DashboardStats(total, completed, cancelled, pending, revenue);
    }
}