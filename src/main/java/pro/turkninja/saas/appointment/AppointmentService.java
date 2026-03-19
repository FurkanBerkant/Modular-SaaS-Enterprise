package pro.turkninja.saas.appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import pro.turkninja.saas.appointment.event.AppointmentApprovedEvent;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void approve(String appointmentId) {
        Appointment appointment = repository.findById(appointmentId).orElseThrow();

        appointment.approve();
        repository.save(appointment);

        publisher.publishEvent(new AppointmentApprovedEvent(
                appointment.getId(),
                appointment.getProviderId(),
                appointment.getCustomerId()
        ));
    }

    @Transactional
    public void respondToAppointment(String appointmentId, boolean isApproved) {
        Appointment appointment = repository.findById(appointmentId).orElseThrow();

        if (isApproved) {
            appointment.approve();
            repository.save(appointment);
            publisher.publishEvent(new AppointmentApprovedEvent(
                    appointment.getId(),
                    appointment.getProviderId(),
                    appointment.getCustomerId()
            ));
        } else {
            appointment.reject();
            repository.save(appointment);
        }
    }
}
