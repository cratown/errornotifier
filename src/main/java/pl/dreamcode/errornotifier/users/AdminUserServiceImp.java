package pl.dreamcode.errornotifier.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

@Service
public class AdminUserServiceImp implements AdminUserService {
    @Autowired
    private UserRepository repository;

    @Override
    public User updateUser(EditForm editForm) throws UserAlreadyExistException {
        Optional<User> userOptional = repository.findById(editForm.getId());
        if(!userOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        if (emailExists(user, editForm.getEmail())) {
            throw new UserAlreadyExistException("E-mail taken: "+ editForm.getEmail());
        }

        user.setEmail(editForm.getEmail());
        return repository.save(user);
    }

    private boolean emailExists(User user, String email) {
        User userByEmail = repository.findByEmail(email);
        return userByEmail != null && userByEmail.getId() != user.getId();
    }

}
