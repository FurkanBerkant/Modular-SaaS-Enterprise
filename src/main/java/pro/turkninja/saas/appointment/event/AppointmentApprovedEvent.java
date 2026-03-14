package pro.turkninja.saas.appointment.event;

public record AppointmentApprovedEvent(String appointmentId, String providerId, String customerId) {
}
