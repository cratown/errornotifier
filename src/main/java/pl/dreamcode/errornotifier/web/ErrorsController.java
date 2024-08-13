package pl.dreamcode.errornotifier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import pl.dreamcode.errornotifier.errors.ErrorRepository;
import pl.dreamcode.errornotifier.errors.Error;

@Controller
@RequestMapping("/")
public class ErrorsController {

    @Autowired
    private ErrorRepository errorRepository;

    @GetMapping
    public String viewHomePage(Model model) {
        model.addAttribute("projectErrors", errorRepository.findAll());
        return "index";
    }

    @GetMapping(value="errors/{id}")
    public String viewDetails(@PathVariable Long id, Model model) {
        Error projectError = errorRepository.getReferenceById(id);
        model.addAttribute("projectError", projectError);
        return "errors/show";
    }

    @GetMapping(value="errors/{id}/delete")
    public RedirectView delete(@PathVariable Long id, @RequestHeader(value = "referer", required = false) String referer) {
        errorRepository.deleteById(id);
        if(referer == null) {
            referer = "/";
        }
        return new RedirectView(referer);
    }
}
