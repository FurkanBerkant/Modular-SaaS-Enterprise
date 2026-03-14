package pro.turkninja.saas.billing;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import pro.turkninja.saas.appointment.event.AppointmentApprovedEvent;

@Service
public class BillingService {

    // Randevu modülünden gelen olayı dinliyoruz!
    @ApplicationModuleListener
    public void onAppointmentApproved(AppointmentApprovedEvent event) {
        System.out.println("Ödeme Modülü haberi aldı! İşletme ID: " + event.providerId());

        // Gidip işletmenin aylık randevu kotasını düşüren veya
        // Stripe/PayTR üzerinden para çeken mantık buraya yazılır.
    }
}