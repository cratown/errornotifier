package pl.dreamcode.errornotifier.notifications;

import pl.dreamcode.errornotifier.errors.OnNewErrorEvent;

public interface NotificationTask {

    public void runNotification(OnNewErrorEvent event);

}
