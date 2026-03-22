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

    /**
     * Belirtilen tarihte dolu olmayan saatleri döner.
     * Dolu saatler veritabanından çekilir, sabit listeden çıkarılır.
     */
    @GetMapping("/available-slots")
    public List<String> getAvailableSlots(
            @RequestParam String providerId,
            @RequestParam String serviceId,
            @RequestParam String date) {

        List<String> allSlots = List.of(
                "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
                "16:00", "16:30", "17:00"
        );

        // O gün o provider'ın dolu saatlerini çek (PENDING + APPROVED)
        List<String> bookedTimes = repository
                .findByProviderIdAndStatusOrderByDateAscTimeAsc(providerId, AppointmentStatus.PENDING)
                .stream()
                .filter(a -> date.equals(a.getDate()))
                .map(Appointment::getTime)
                .toList();

        List<String> approvedTimes = repository
                .findByProviderIdAndStatusOrderByDateAscTimeAsc(providerId, AppointmentStatus.APPROVED)
                .stream()
                .filter(a -> date.equals(a.getDate()))
                .map(Appointment::getTime)
                .toList();

        return allSlots.stream()
                .filter(slot -> !bookedTimes.contains(slot) && !approvedTimes.contains(slot))
                .toList();
    }

    /**
     * Randevu oluşturur. CSRF token header üzerinden gelir (booking-widget.html'de set ediliyor).
     */
    @PostMapping("/book")
    public ResponseEntity<?> book(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal AppUser currentUser) {

        try {
            String message = bookingService.bookAppointment(
                    request.providerId(),
                    currentUser.getId(),
                    request.date(),
                    request.time()
            );
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── Request DTO ───────────────────────────────────────────────────────────
    record BookingRequest(
            String providerId,
            String serviceId,
            String date,
            String time
    ) {}
}