package pl.dreamcode.errornotifier.web.admin;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import pl.dreamcode.errornotifier.users.AdminUserService;
import pl.dreamcode.errornotifier.users.EditForm;
import pl.dreamcode.errornotifier.users.RegistrationService;
import pl.dreamcode.errornotifier.users.User;
import pl.dreamcode.errornotifier.users.UserRepository;
import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

@Controller
@RequestMapping("/admin/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminUserService userService;

    @GetMapping
    public String viewHomePage(Model model, @RequestParam("page") Optional<Integer> page) {
        Integer currentPage = page.orElse(1)-1;
        Pageable pageable = PageRequest.of(currentPage, 10, Sort.by("id").descending());
        Page<User> users = userRepository.findAll(pageable);
        model.addAttribute("users", users);
        int totalPages = users.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "admin/users/index";
    }

    @GetMapping(value="{id}")
    public String viewDetails(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        }
        EditForm user = new EditForm(userOptional.get().getId());
        user.setEmail(userOptional.get().getEmail());
        model.addAttribute("user", user);
        return "admin/users/edit";
    }

    @PutMapping(value="{id}")
    public String registerUserAccount(@ModelAttribute("user") @Valid EditForm form, HttpServletRequest request, Errors errors, Model model) {
        model.addAttribute("user", form);
        try {
            User registered = userService.updateUser(form);
            model.addAttribute("success", true);
        } catch (UserAlreadyExistException uaeEx) {
            model.addAttribute("registrationError", uaeEx.getMessage());
            return "admin/users/edit";
        }
        return "admin/users/edit";
    }

    @GetMapping(value="{id}/delete")
    public RedirectView delete(@PathVariable Long id, @RequestHeader(value = "referer", required = false) String referer) {
        userRepository.deleteById(id);
        if(referer == null) {
            referer = "/";
        }
        return new RedirectView(referer);
    }
}
