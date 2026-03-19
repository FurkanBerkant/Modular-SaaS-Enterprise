package pro.turkninja.saas.appointment;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "appointments")
@Data
public class Appointment {

    @Id
    private String id;
    private String providerId;
    private String customerId;
    private TimeSlot timeSlot;
    private String date;
    private String time;

    private AppointmentStatus status = AppointmentStatus.PENDING;
    @Version
    private Long version;

    public void approve() {
        if (this.status != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Sadece BEKLEYEN randevular onaylanabilir! Şu anki durum: " + this.status);
        }
        this.status = AppointmentStatus.APPROVED;
    }

    public void reject() {
        if (this.status != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Sadece BEKLEYEN randevular reddedilebilir! Şu anki durum: " + this.status);
        }
        this.status = AppointmentStatus.REJECTED;
    }
}
