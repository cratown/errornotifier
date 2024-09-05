package pl.dreamcode.errornotifier.errors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ErrorDBService implements ErrorService {

    @Autowired
    private ErrorRepository errorRepository;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Override
    public Error create(Error newError) {
        Error error = errorRepository.save(newError);
        eventPublisher.publishEvent(new OnNewErrorEvent(error));
        return error;
    }

}
