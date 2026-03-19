package pro.turkninja.saas.appointment;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByProviderIdAndStatusOrderByDateAscTimeAsc(String providerId, AppointmentStatus status);
}
