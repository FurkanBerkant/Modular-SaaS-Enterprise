package pro.turkninja.saas.provider;

import pro.turkninja.saas.appointment.Appointment;

import java.util.List;

public record CustomerCrmProfile(
        ClientRecord record,
        List<Appointment> history
) {}
