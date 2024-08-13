package pl.dreamcode.errornotifier.errors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.dreamcode.errornotifier.notifications.NotificationTask;

@Service
public class ErrorDBService implements ErrorService {

    @Autowired
    private ErrorRepository errorRepository;

    @Autowired
    private NotificationTask notificationTask;

    @Override
    public Error create(Error newError) {
        Error error = errorRepository.save(newError);
        notificationTask.runNotification(error.getProjectName());
        return error;
    }

}
