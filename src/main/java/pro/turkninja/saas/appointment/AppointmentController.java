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

    @GetMapping("/available-slots")
    public List<String> getAvailableSlots(
            @RequestParam String providerId,
            @RequestParam String serviceId,
            @RequestParam String date) {

        System.out.println(date + " tarihi için boş saatler hesaplanıyor...");

        return List.of("09:00", "10:30", "13:00", "15:00", "16:30");
    }

    @PostMapping("/book")
    public ResponseEntity<?> book(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal AppUser currentUser) {

        try {
            String message = bookingService.bookAppointment(
                    request.providerId(),
                    currentUser.getId(),   // customerId = oturum açan kullanıcı
                    request.date(),
                    request.time()
            );
            return ResponseEntity.ok(Map.of("message", message));

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
record BookingRequest(
        String providerId,
        String serviceId,
        String date,
        String time
) {}