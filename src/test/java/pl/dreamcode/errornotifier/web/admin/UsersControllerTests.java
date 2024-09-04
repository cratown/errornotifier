package pl.dreamcode.errornotifier.web.admin;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import pl.dreamcode.errornotifier.TestWebSecurityConfig;
import pl.dreamcode.errornotifier.users.AdminUserService;
import pl.dreamcode.errornotifier.users.EditForm;
import pl.dreamcode.errornotifier.users.User;
import pl.dreamcode.errornotifier.users.UserRepository;
import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(value = UsersController.class)
@Import({TestWebSecurityConfig.class})
public class UsersControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AdminUserService userService;

    @Test
    void shouldNotAllowAccessForAnonymousUser() throws Exception {
        mockMvc
            .perform(get("/admin/users"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", containsString("/login")));
    }

    @Test
    @WithUserDetails("user")
    void shouldAllowAccessForAuthenticatedUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        given(userRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<User>(Arrays.asList(user)));

        mockMvc
            .perform(get("/admin/users"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admin/users/index"))
            .andExpect(content().string(containsString("Users")))
            .andExpect(content().string(containsString("test@example.com")));
    }

    @Test
    void shouldNotAllowAccessForAnonymousUserToDetailsPage() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);

        mockMvc
            .perform(get("/admin/users/{id}", user.getId()))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", containsString("/login")));
    }

    @Test
    @WithUserDetails("user")
    void shouldAllowAccessForAuthenticatedUserToDetailsPage() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);
        given(userRepository.findById(user.getId()))
				.willReturn(Optional.of(user));

        mockMvc
            .perform(get("/admin/users/{id}", user.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("admin/users/edit"))
            .andExpect(model().attributeExists("user"))
            .andExpect(content().string(containsString(user.getEmail())));
    }

    @Test
    @WithUserDetails("user")
    void shouldReturnNotFoundErrorOnDetails() throws Exception {
        mockMvc
            .perform(get("/admin/users/{id}", 1))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotAllowAccessForAnonymousUserToUpdateUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);

        mockMvc
            .perform(put("/admin/users/{id}", user.getId())
                .param("email", "test1@example.com")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", containsString("/login")));
    }

    @Test
    @WithUserDetails("user")
    void shouldAllowAccessForAuthenticatedUserToUpdateUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);

        mockMvc
            .perform(put("/admin/users/{id}", user.getId())
                .param("email", "test1@example.com")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("success"))
            .andExpect(content().string(containsString("Updated")));
    }

    @Test
    @WithUserDetails("user")
    void shouldNotAllowToChangeMailToExistedInOtherUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);

        given(userService.updateUser(any(EditForm.class)))
				.willThrow(new UserAlreadyExistException("E-mail taken: "));

        mockMvc
            .perform(put("/admin/users/{id}", user.getId())
                .param("email", "test1@example.com")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("registrationError"));
    }

    @Test
    void shouldNotAllowAccessForAnonymousUserToDelete() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);

        mockMvc
            .perform(get("/admin/users/{id}/delete", user.getId()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", containsString("/login")));
    }

    @Test
    @WithUserDetails("user")
    void shouldAllowAccessForAuthenticateUserToDelete() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);

        mockMvc
            .perform(get("/admin/users/{id}/delete", user.getId()))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
        verify(userRepository).deleteById(user.getId());
    }
}
