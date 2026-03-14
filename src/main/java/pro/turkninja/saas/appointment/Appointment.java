package pro.turkninja.saas.appointment;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "appointments")
@Data
public class Appointment {

    @Id
    private String id;
    private String providerId;
    private String customerId;
    private TimeSlot timeSlot;

    private AppointmentStatus status; // PENDING, APPROVED, CANCELLED

    public void approve() {
        if (this.status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("İptal edilmiş bir randevu onaylanamaz!");
        }
        this.status = AppointmentStatus.APPROVED;
    }
    public record TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        public TimeSlot {
            if (endTime.isBefore(startTime)) {
                throw new IllegalArgumentException("Bitiş zamanı, başlangıçtan önce olamaz!");
            }
        }
    }
}
