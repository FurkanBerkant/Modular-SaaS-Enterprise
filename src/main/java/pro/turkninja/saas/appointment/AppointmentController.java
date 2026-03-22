package pro.turkninja.saas.appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pro.turkninja.saas.security.AppUser;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final BookingService bookingService;
    private final AppointmentRepository repository;

    private static final List<String> ALL_SLOTS = List.of(
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00"
    );

    @GetMapping("/available-slots")
    public List<String> getAvailableSlots(
            @RequestParam String providerId,
            @RequestParam String serviceId,
            @RequestParam String date) {

        // ✅ Tek sorguda o günün dolu saatleri — N+1 yok
        List<String> bookedTimes = repository
                .findByProviderIdAndDateAndStatusIn(
                        providerId, date,
                        List.of(AppointmentStatus.PENDING, AppointmentStatus.APPROVED))
                .stream()
                .map(Appointment::getTime)
                .toList();

        return ALL_SLOTS.stream()
                .filter(slot -> !bookedTimes.contains(slot))
                .toList();
    }

    @PostMapping("/book")
    public ResponseEntity<?> book(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal AppUser currentUser) {

        try {
            String message = bookingService.bookAppointment(
                    request.providerId(),
                    currentUser.getId(),
                    request.date(),
                    request.time(),
                    request.serviceId()
            );
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    record BookingRequest(
            String providerId,
            String serviceId,
            String date,
            String time
    ) {}
}