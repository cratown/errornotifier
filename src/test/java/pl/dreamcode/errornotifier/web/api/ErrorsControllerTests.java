package pl.dreamcode.errornotifier.web.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import pl.dreamcode.errornotifier.errors.ErrorService;
import pl.dreamcode.errornotifier.TestWebSecurityConfig;
import pl.dreamcode.errornotifier.errors.Error;

@WebMvcTest(value = ErrorsController.class)
@Import({TestWebSecurityConfig.class})
public class ErrorsControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErrorService errorService;

    @Test
    void shouldAllowAccessForAnonymousUser() throws Exception {
        Error error = new Error();
        error.setProjectName("error_notifier");
        error.setBody("body on error");
        
        given(errorService.create(any(Error.class)))
				.willReturn(error);

        mockMvc
            .perform(post("/errors.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(error))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());
        // Check if repository save called
        verify(errorService).create(any(Error.class));
    }

    @Test
    void shouldReturnBadRequestWithWrongBody() throws Exception {
        mockMvc
            .perform(post("/errors.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("projectName").value("must not be blank"))
            .andExpect(jsonPath("body").value("must not be null"));
    }

    protected static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
