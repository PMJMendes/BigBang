package bigBang.module.mainModule.client.notifications;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.Notification;
import bigBang.library.shared.event.NewNotificationEvent;
import bigBang.library.shared.event.NewNotificationEventHandler;
import bigBang.module.mainModule.client.userInterface.InfoPanel;
import bigBang.module.mainModule.client.userInterface.InfoPanel.InfoPanelType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public class NotificationsManager {
	
	private EventBus eventBus;
	private com.google.gwt.event.shared.HandlerRegistration newNotificationHandlerRegistration = null;

	public NotificationsManager(EventBus eventBus){
		this.eventBus = eventBus;
	}
	
	
	/*
	 * TRAY NOTIFICATIONS START
	 */
	
	public void showTrayNotification(final Notification notification){
		if(notification.getCaption() != null || notification.getContent() != null) {
			InfoPanel info = new InfoPanel(notification.getCaption(), notification.getContent(), new ClickHandler() {
				
				public void onClick(ClickEvent event) {
					if(notification.getEvent() != null)
						eventBus.fireEvent(notification.getEvent());
				}
			}); 
			InfoPanel.show(InfoPanelType.TRAY_NOTIFICATION, info);
			return;
		}
		
		InfoPanel info = new InfoPanel(new Button("here"), new ClickHandler() {
		
			public void onClick(ClickEvent event) {
				if(notification.getEvent() != null)
					eventBus.fireEvent(notification.getEvent());
			}
		}); 
		InfoPanel.show(InfoPanelType.TRAY_NOTIFICATION, info);
	}

	public void enableTrayNotifications(){
		setTrayNotificationsEnabled(true);
	}
	
	public void disableTrayNotifications(){
		setTrayNotificationsEnabled(false);
	}
	
	private void setTrayNotificationsEnabled(boolean enabled) {
		if(enabled){
			if(!isTrayNotificationsEnabled()){
				this.newNotificationHandlerRegistration = eventBus.addHandler(NewNotificationEvent.TYPE, new NewNotificationEventHandler() {
					
					public void onNewNotification(NewNotificationEvent event) {
						
						showTrayNotification(event.getNotification());
					}
				});
			}
		}else{
			if(isTrayNotificationsEnabled()){
				this.newNotificationHandlerRegistration.removeHandler();
				this.newNotificationHandlerRegistration = null;
			}
		}
	}
	
	public boolean isTrayNotificationsEnabled(){
		return this.newNotificationHandlerRegistration != null;
	}
	
	
	/*
	 * BADGE NOTIFICATIONS START
	 */
	/*
	public void registerTextBadge(final TextBadge badge) {
		
		this.eventBus.addHandler(NewNotificationEvent.TYPE, new NewNotificationEventHandler() {
			
			public void onNewNotification(NewNotificationEvent event) {
				badge.setText((event.hashCode() + "").substring(0, 2));
			}
		});
	}
	*/
	
}
