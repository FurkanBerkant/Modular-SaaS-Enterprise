package pro.turkninja.saas.appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pro.turkninja.saas.security.AppUser;
import pro.turkninja.saas.security.UserRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeDashboardController {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @GetMapping("/my-schedule")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<Appointment> getMyDailySchedule(@RequestParam String date) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser employee = userRepository.findByEmail(email).orElseThrow();

        return appointmentRepository.findByEmployeeIdAndDateOrderByTimeAsc(employee.getId(), date);
    }

    @PostMapping("/complete-appointment/{appointmentId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Map<String, String>> markAsCompleted(@PathVariable String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();

        try {
            appointment.complete();
            appointmentRepository.save(appointment);
            return ResponseEntity.ok(Map.of("message", "Randevu tamamlandı olarak işaretlendi."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}