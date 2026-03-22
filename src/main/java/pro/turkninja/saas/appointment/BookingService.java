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

    // ✅ serviceId parametresi eklendi
    public String bookAppointment(String providerId, String customerId,
                                  String date, String time, String serviceId) {
        try {
            Appointment newAppointment = new Appointment();
            newAppointment.setProviderId(providerId);
            newAppointment.setCustomerId(customerId);
            newAppointment.setDate(date);
            newAppointment.setTime(time);
            newAppointment.setServiceId(serviceId); // ✅ artık kaydediliyor
            newAppointment.setStatus(AppointmentStatus.PENDING);

            repository.save(newAppointment);
            return "Randevu talebiniz başarıyla alındı. İşletmenin onayı bekleniyor.";

        } catch (DuplicateKeyException | OptimisticLockingFailureException e) {
            throw new IllegalStateException(
                    "Üzgünüz, bu saat dilimi az önce başka bir kullanıcı tarafından " +
                            "rezerve edildi. Lütfen başka bir saat seçiniz.");
        }
    }
}