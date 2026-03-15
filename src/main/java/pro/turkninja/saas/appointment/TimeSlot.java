package pro.turkninja.saas.appointment;

import java.time.LocalDateTime;

public record TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
    public TimeSlot {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("Bitiş zamanı, başlangıçtan önce olamaz!");
        }
    }
}
