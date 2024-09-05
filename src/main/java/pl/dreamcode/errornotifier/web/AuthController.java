package pl.dreamcode.errornotifier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.Valid;
import pl.dreamcode.errornotifier.users.OnRegistrationCompleteEvent;
import pl.dreamcode.errornotifier.users.RegistrationForm;
import pl.dreamcode.errornotifier.users.RegistrationService;
import pl.dreamcode.errornotifier.users.User;
import pl.dreamcode.errornotifier.users.exception.InvalidTokenException;
import pl.dreamcode.errornotifier.users.exception.TokenExpiredException;
import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

@Controller
public class AuthController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    // Display login form
    @RequestMapping("/login")
    public String login() {
        return "auth/login";
    }

    // Display validation form
    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        RegistrationForm registrationForm = new RegistrationForm();
        model.addAttribute("user", registrationForm);
        return "auth/registration";
    }

    // Validate and process registration form
    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") @Valid RegistrationForm registrationForm, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("registrationError", "Please correct the errors.");
            return "auth/registration";
        }
        try {
            User user = registrationService.registerNewUserAccount(registrationForm);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
            model.addAttribute("success", "Account created.");
        } catch (UserAlreadyExistException uaeEx) {
            model.addAttribute("registrationError", "An account for that username/email already exists.");
            return "auth/registration";
        }
        return "auth/registration";
    }

    // Process registration confirmation link and activate user if success
    @GetMapping("/regitrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, Model model) {
        try {
            registrationService.confirm(token);
            model.addAttribute("success", "Account activated.");
        } catch (InvalidTokenException | TokenExpiredException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "auth/registration_confirm"; 
    }
}
