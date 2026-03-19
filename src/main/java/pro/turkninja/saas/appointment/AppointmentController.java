package pro.turkninja.saas.appointment;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @GetMapping("/available-slots")
    public List<String> getAvailableSlots(
            @RequestParam String providerId,
            @RequestParam String serviceId,
            @RequestParam String date) {

        System.out.println(date + " tarihi için boş saatler hesaplanıyor...");

        return List.of("09:00", "10:30", "13:00", "15:00", "16:30");
    }

}
