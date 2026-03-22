package pro.turkninja.saas.appointment;

public record DashboardStats(
        int totalAppointments,
        int completedAppointments,
        int cancelledAppointments,
        int pendingAppointments,
        double estimatedRevenue
) {
    public int completionRate() {
        if (totalAppointments == 0) return 0;
        return (int) Math.round((completedAppointments * 100.0) / totalAppointments);
    }
}
