package pro.turkninja.saas.provider;

import pro.turkninja.saas.appointment.AppointmentSummary;

import java.util.List;

public record CustomerCrmProfile(
        ClientRecord record,
        List<AppointmentSummary> history
) {}