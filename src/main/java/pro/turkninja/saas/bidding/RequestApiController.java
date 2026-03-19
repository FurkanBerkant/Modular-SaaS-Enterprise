package pro.turkninja.saas.bidding;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pro.turkninja.saas.security.AppUser;
import pro.turkninja.saas.security.UserRepository;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestApiController {

    private final ServiceRequestRepository requestRepository;
    private final UserRepository userRepository;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String createRequest(@RequestBody ServiceRequest payload) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser customer = userRepository.findByEmail(email).orElseThrow();

        payload.setCustomerId(customer.getId());
        payload.setStatus(RequestStatus.OPEN);

        requestRepository.save(payload);

        return "İlanınız başarıyla yayına alındı! İşletmelerden teklif bekleyebilirsiniz.";
    }
}