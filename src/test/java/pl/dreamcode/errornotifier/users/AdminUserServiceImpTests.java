package pl.dreamcode.errornotifier.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;


@ExtendWith(MockitoExtension.class)
public class AdminUserServiceImpTests {

    @InjectMocks
    private AdminUserServiceImp service;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldUpdateUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);
        EditForm form = new EditForm(user.getId());
        form.setEmail(user.getEmail());

        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        User savedUser = service.updateUser(form);
        assertEquals(savedUser.getId(), user.getId());
    }

    @Test
    void shouldThrowExceptionWhenNoUserFound() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);
        EditForm form = new EditForm(user.getId());
        form.setEmail(user.getEmail());

        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.updateUser(form));
    }

    @Test
    void shouldThrowExceptionWhenOtherUserHasSameEmail() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);
        User otherUser = new User();
        otherUser.setEmail("test@example.com");
        otherUser.setId(2l);
        EditForm form = new EditForm(user.getId());
        form.setEmail(user.getEmail());

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByEmail(user.getEmail())).willReturn(otherUser);

        assertThrows(UserAlreadyExistException.class, () -> service.updateUser(form));
    }

}
