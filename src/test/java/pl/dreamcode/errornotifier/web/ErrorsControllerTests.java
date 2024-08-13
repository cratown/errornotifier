package pl.dreamcode.errornotifier.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import pl.dreamcode.errornotifier.errors.ErrorRepository;
import pl.dreamcode.errornotifier.TestWebSecurityConfig;
import pl.dreamcode.errornotifier.errors.Error;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;

@WebMvcTest(value = ErrorsController.class)
@Import({TestWebSecurityConfig.class})
public class ErrorsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErrorRepository errorRepository;

    @Test
    void shouldNotAllowAccessForAnonymousUser() throws Exception {
        mockMvc
            .perform(get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", containsString("/login")));
    }

    @Test
    @WithUserDetails("user")
    void shouldAllowAccessForAuthenticatedUser() throws Exception {
        Error error = new Error();
        error.setProjectName("error_notifier");
        given(errorRepository.findAll())
				.willReturn(Arrays.asList(error));

        mockMvc
            .perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("projectErrors"))
            .andExpect(content().string(containsString("Errors List")))
            .andExpect(content().string(containsString("error_notifier")));
    }

    @Test
    void shouldNotAllowAccessForAnonymousUserToDetailsPage() throws Exception {
        Error error = new Error();
        error.setProjectName("error_notifier");
        error.setId(1l);
        error.setBody("Random body");

        mockMvc
            .perform(get("/errors/{id}", error.getId()))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", containsString("/login")));
    }

    @Test
    @WithUserDetails("user")
    void shouldAllowAccessForAuthenticatedUserToDetailsPage() throws Exception {
        Error error = new Error();
        error.setProjectName("error_notifier");
        error.setId(1l);
        error.setBody("Random body");
        given(errorRepository.getReferenceById(error.getId()))
				.willReturn(error);

        mockMvc
            .perform(get("/errors/{id}", error.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("errors/show"))
            .andExpect(model().attributeExists("projectError"))
            .andExpect(content().string(containsString(error.getProjectName())))
            .andExpect(content().string(containsString(error.getBody())));
    }

    @Test
    void shouldNotAllowAccessForAnonymousUserToDeleteError() throws Exception {
        Error error = new Error();
        error.setProjectName("error_notifier");
        error.setId(1l);

        mockMvc
            .perform(get("/errors/{id}/delete", error.getId()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", containsString("/login")));
    }

    @Test
    @WithUserDetails("user")
    void shouldAllowAccessForAuthenticateUserToDeleteError() throws Exception {
        Error error = new Error();
        error.setProjectName("error_notifier");
        error.setId(1l);

        mockMvc
            .perform(get("/errors/{id}/delete", error.getId()))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
        verify(errorRepository).deleteById(error.getId());
    }
}
