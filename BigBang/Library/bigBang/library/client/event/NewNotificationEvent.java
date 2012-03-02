package bigBang.library.client.event;

import bigBang.library.client.Notification;

import com.google.gwt.event.shared.GwtEvent;

public class NewNotificationEvent extends GwtEvent<NewNotificationEventHandler> {
	public static Type<NewNotificationEventHandler> TYPE = new Type<NewNotificationEventHandler>();
	private Notification notification;
	private Notification.TYPE notificationType;

	public NewNotificationEvent(Notification notification, Notification.TYPE type){
		this.notification = notification;
		this.notificationType = type;
	}

	public Notification getNotification(){
		return this.notification;
	}

	public Notification.TYPE getTYPE(){
		return this.notificationType;
	}
	
	@Override
	public Type<NewNotificationEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewNotificationEventHandler handler) {
		handler.onNewNotification(this);
	}
}