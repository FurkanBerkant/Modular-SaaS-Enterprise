package pro.turkninja.saas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pro.turkninja.saas.appointment.AppointmentRepository;
import pro.turkninja.saas.appointment.Appointment;
import pro.turkninja.saas.provider.ProviderRepository;
import pro.turkninja.saas.security.AppUser;

import java.time.LocalDate;
import java.util.List;

/**
 * Çalışan dashboard'ının UI controller'ı.
 * REST endpoint'leri (my-schedule, complete) EmployeeDashboardController'da.
 */
@Controller
@RequiredArgsConstructor
public class EmployeeUIController {

    private final AppointmentRepository appointmentRepository;
    private final ProviderRepository providerRepository;

    @GetMapping("/employee/dashboard")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String employeeDashboard(
            @AuthenticationPrincipal AppUser employee,
            Model model) {

        String today = LocalDate.now().toString();

        List<Appointment> appointments = appointmentRepository
                .findByEmployeeIdAndDateOrderByTimeAsc(employee.getId(), today);
        model.addAttribute("appointments", appointments);

        if (employee.getProviderId() != null) {
            providerRepository.findById(employee.getProviderId())
                    .ifPresent(p -> model.addAttribute("provider", p));
        }

        return "employee/dashboard";
    }
}