package bigBang.library.shared.event;

import com.google.gwt.event.shared.EventHandler;

public interface NewNotificationEventHandler extends EventHandler {
	void onNewNotification(NewNotificationEvent event);
}
