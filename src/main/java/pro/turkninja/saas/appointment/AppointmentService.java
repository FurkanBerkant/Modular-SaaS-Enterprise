package pro.turkninja.saas.appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.turkninja.saas.appointment.event.AppointmentApprovedEvent;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;
    private final ApplicationEventPublisher publisher; // Spring'in yerleşik megafonu

    @Transactional
    public void approve(String appointmentId) {
        Appointment appointment = repository.findById(appointmentId).orElseThrow();

        appointment.approve(); // DDD kuralımız (Önceki dersten)
        repository.save(appointment);

        // Randevu onaylandıktan hemen sonra olayı fırlatıyoruz!
        publisher.publishEvent(new AppointmentApprovedEvent(
                appointment.getId(),
                appointment.getProviderId(),
                appointment.getCustomerId()
        ));
    }
}
