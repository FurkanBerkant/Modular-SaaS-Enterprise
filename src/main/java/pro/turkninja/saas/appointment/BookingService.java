package pro.turkninja.saas.appointment;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final AppointmentRepository repository;

    public BookingService(AppointmentRepository repository) {
        this.repository = repository;
    }

    public String bookAppointment(String providerId, String customerId, String date, String time) {
        try {
            Appointment newAppointment = new Appointment();
            newAppointment.setProviderId(providerId);
            newAppointment.setCustomerId(customerId);
            newAppointment.setDate(date);
            newAppointment.setTime(time);
            newAppointment.setStatus(AppointmentStatus.PENDING); // Onay bekliyor

            repository.save(newAppointment);
            return "Randevu talebiniz başarıyla alındı. İşletmenin onayı bekleniyor.";

        } catch (DuplicateKeyException | OptimisticLockingFailureException e) {
            throw new IllegalStateException("Üzgünüz, bu saat dilimi az önce başka bir kullanıcı tarafından rezerve edildi. Lütfen başka bir saat seçiniz.");
        }
    }
}
