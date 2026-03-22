package pro.turkninja.saas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pro.turkninja.saas.appointment.AppointmentRepository;
import pro.turkninja.saas.security.AppUser;

import java.util.Map;

/**
 * Çalışan (EMPLOYEE) REST API endpoint'leri.
 * UI mapping'ler ProviderUIController'da.
 *
 * AppointmentRepository'ye doğrudan erişim burada kabul edilebilir:
 * bu controller appointment bounded context'ine hizmet eden bir REST API.
 * İleride appointment modülü büyürse AppointmentQueryService'e taşınır.
 */
@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeDashboardController {

    private final AppointmentRepository appointmentRepository;

    /**
     * Çalışanın belirtilen tarihteki randevularını döner.
     * UI dashboard bunu kullanmak yerine server-side model kullanıyor;
     * bu endpoint mobil veya harici istemciler için açık bırakıldı.
     */
    @GetMapping("/my-schedule")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Object getMyDailySchedule(
            @RequestParam String date,
            @AuthenticationPrincipal AppUser employee) {

        return appointmentRepository
                .findByEmployeeIdAndDateOrderByTimeAsc(employee.getId(), date);
    }

    /**
     * Randevuyu tamamlandı olarak işaretler.
     */
    @PostMapping("/complete-appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'PROVIDER')")
    public ResponseEntity<Map<String, String>> markAsCompleted(
            @PathVariable String appointmentId) {

        var appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Randevu bulunamadı: " + appointmentId));

        try {
            appointment.complete();
            appointmentRepository.save(appointment);
            return ResponseEntity.ok(Map.of("message", "Randevu tamamlandı olarak işaretlendi."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}