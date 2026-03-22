package pro.turkninja.saas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pro.turkninja.saas.security.AppUser;
import pro.turkninja.saas.security.UserRepository;
import pro.turkninja.saas.security.UserRole;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String businessName,
            @RequestParam String phone,
            Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Şifreler eşleşmiyor.");
            return "register";
        }

        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Bu e-posta adresi zaten kayıtlı.");
            return "register";
        }

        AppUser newUser = new AppUser();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(UserRole.CUSTOMER);
        userRepository.save(newUser);

        return "redirect:/login?registered=true&onboarding=true";
    }
}