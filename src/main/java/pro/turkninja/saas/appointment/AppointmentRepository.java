package pro.turkninja.saas.appointment;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByProviderIdAndStatusOrderByDateAscTimeAsc(String providerId, AppointmentStatus status);
    List<Appointment> findByProviderIdAndCustomerIdOrderByDateDesc(String providerId, String customerId);
    List<Appointment> findByEmployeeIdAndDateOrderByTimeAsc(String id, String date);
    @Aggregation(pipeline = {
            "{ '$match': { 'providerId': ?0 } }",
            "{ '$group': { " +
                    "'_id': '$status', " +
                    "'count': { '$sum': 1 }" +
                    "} }"
    })
    List<StatusCountDTO> countAppointmentsByStatus(String providerId);
    List<Appointment> findByProviderIdAndStatusAndDateStartingWith(
            String providerId, AppointmentStatus status, String yearMonth);

}
