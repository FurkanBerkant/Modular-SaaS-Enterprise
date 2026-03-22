package pro.turkninja.saas.appointment;

public record AppointmentSummary(
        String id,
        String providerId,
        String customerId,
        String serviceId,
        String date,
        String time,
        AppointmentStatus status
) {}