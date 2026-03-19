package pro.turkninja.saas.appointment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerUIController {

    @GetMapping("/booking")
    public String bookingWidget() {
        return "customer/booking-widget";
    }

    @GetMapping("/request")
    public String newRequest() {
        return "customer/new-request";
    }
}
