package pl.dreamcode.errornotifier.web;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import pl.dreamcode.errornotifier.errors.ErrorRepository;
import pl.dreamcode.errornotifier.errors.Error;

@Controller
@RequestMapping("/")
public class ErrorsController {

    @Autowired
    private ErrorRepository errorRepository;

    @GetMapping
    public String viewHomePage(Model model, @RequestParam("page") Optional<Integer> page) {
        Integer currentPage = page.orElse(1)-1;
        Pageable pageable = PageRequest.of(currentPage, 10, Sort.by("id").descending());
        Page<Error> projectErrors = errorRepository.findAll(pageable);
        model.addAttribute("projectErrors", projectErrors);
        int totalPages = projectErrors.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

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
