package pl.dreamcode.errornotifier.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;

import pl.dreamcode.errornotifier.TestWebSecurityConfig;
import pl.dreamcode.errornotifier.users.OnPasswordResetRequestEvent;
import pl.dreamcode.errornotifier.users.OnRegistrationCompleteEvent;
import pl.dreamcode.errornotifier.users.PasswordChangeForm;
import pl.dreamcode.errornotifier.users.PasswordResetService;
import pl.dreamcode.errornotifier.users.RegistrationForm;
import pl.dreamcode.errornotifier.users.RegistrationService;
import pl.dreamcode.errornotifier.users.User;
import pl.dreamcode.errornotifier.users.exception.InvalidTokenException;
import pl.dreamcode.errornotifier.users.exception.TokenExpiredException;
import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(value = AuthController.class)
@Import({TestWebSecurityConfig.class})
@RecordApplicationEvents
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private PasswordResetService passwordResetService;

    @Autowired
	ApplicationEvents events;

    @Test
    void shouldAllowAccessForAnonymousUserToLoginPage() throws Exception {
        mockMvc
            .perform(get("/login"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldAllowAccessForAnonymousUserToRegistrationPage() throws Exception {
        mockMvc
            .perform(get("/registration"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateRegisterUser() throws Exception {
        String password = "superSecurePassword";
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@example.com");
        registrationForm.setPassword(password);
        registrationForm.setMatchingPassword(password);
        
        User user = new User();
        user.setEmail(registrationForm.getEmail());
        user.setId(1l);

        given(registrationService.registerNewUserAccount(any(RegistrationForm.class))).willReturn(user);
        
        mockMvc
            .perform(post("/registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", registrationForm.getEmail())
                .param("password", registrationForm.getPassword())
                .param("matchingPassword", registrationForm.getMatchingPassword())
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Account created")));

        verify(registrationService).registerNewUserAccount(any(RegistrationForm.class));
        long numEvents = events.stream(OnRegistrationCompleteEvent.class).count(); 
		assertEquals(1, numEvents);
    }

    @Test
    void shouldDisplayValidationErrors() throws Exception {
        mockMvc
            .perform(post("/registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "")
                .param("password", "")
                .param("matchingPassword", "")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Please correct the errors")));
    }

    @Test
    void shouldDisplayErrorAboutExistingAccount() throws Exception {

        String password = "superSecurePassword";
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@example.com");
        registrationForm.setPassword(password);
        registrationForm.setMatchingPassword(password);

        given(registrationService.registerNewUserAccount(any(RegistrationForm.class))).willThrow(new UserAlreadyExistException("There is an account with that email address"));

        mockMvc
            .perform(post("/registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", registrationForm.getEmail())
                .param("password", registrationForm.getPassword())
                .param("matchingPassword", registrationForm.getMatchingPassword())
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("An account for that username/email already exists")));
    }

    @Test
    void shouldDisplayErrorWhenSomethingWrongWithToken() throws Exception {

        String token = UUID.randomUUID().toString();

        given(registrationService.confirm(token)).willThrow(new InvalidTokenException());

        mockMvc
            .perform(get("/regitrationConfirm?token={token}", token))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Invalid token")));
    }

    @Test
    void shouldDisplayErrorWhenExpiredToken() throws Exception {

        String token = UUID.randomUUID().toString();

        given(registrationService.confirm(token)).willThrow(new TokenExpiredException());

        mockMvc
            .perform(get("/regitrationConfirm?token={token}", token))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Token expired")));
    }

    @Test
    void shouldDisplaySuccessMessageWhenUserConfirmed() throws Exception {

        String token = UUID.randomUUID().toString();

        given(registrationService.confirm(token)).willReturn(any(User.class));

        mockMvc
            .perform(get("/regitrationConfirm?token={token}", token))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Account activated")));

        verify(registrationService).confirm(token);
    }

    @Test
    void shouldDisplayPasswordResetForm() throws Exception {
        mockMvc
            .perform(get("/passwordReset"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Forgot your password?")));
    }

    @Test
    void shouldProcessPasswordResetFormAndDisplaySuccessMessage() throws Exception {
        mockMvc
            .perform(post("/passwordReset")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test@example.com")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Check your inbox for the next steps")));
        
        long numEvents = events.stream(OnPasswordResetRequestEvent.class).count(); 
        assertEquals(1, numEvents);
    }

    @Test
    void shouldDisplayValidationErrorsOnPasswordReset() throws Exception {
        String token = UUID.randomUUID().toString();

        mockMvc
            .perform(post("/passwordReset?token={token}", token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("must not be blank")));
    }

    @Test
    void shouldDisplayPasswordChangeForm() throws Exception {
        String token = UUID.randomUUID().toString();

        given(passwordResetService.findUserByToken(token)).willReturn(new User());
        
        mockMvc
            .perform(get("/changePassword?token={token}", token))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Password confirmation")));
    }

    @Test
    void shouldDisplayPasswordChangeFormWithMessageWhenInvalidToken() throws Exception {
        String token = UUID.randomUUID().toString();

        given(passwordResetService.findUserByToken(token)).willReturn(null);
        
        mockMvc
            .perform(get("/changePassword?token={token}", token))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Invalid token")));
    }

    @Test
    void shouldChangePasswordAndRedirectToLoginPageOnValidationSuccess() throws Exception {
        String token = UUID.randomUUID().toString();
        String password = "superSecretPassword";
        PasswordChangeForm resetPasswordForm = new PasswordChangeForm();

        given(passwordResetService.updateUserPassword(token, resetPasswordForm)).willReturn(new User());
        
        mockMvc
            .perform(post("/changePassword?token={token}", token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("password", password)
                .param("matchingPassword", password)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", containsString("/login")));

        verify(passwordResetService).updateUserPassword(eq(token), any(PasswordChangeForm.class));
    }

    @Test
    void shouldDisplayValidationErrorsOnChangePassword() throws Exception {
        String token = UUID.randomUUID().toString();

        mockMvc
            .perform(post("/changePassword?token={token}", token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("password", "")
                .param("matchingPassword", "")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("must not be blank")));
    }
}
