package pl.dreamcode.errornotifier.users;

import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

public interface AdminUserService {
    User updateUser(EditForm editForm) throws UserAlreadyExistException;
}
