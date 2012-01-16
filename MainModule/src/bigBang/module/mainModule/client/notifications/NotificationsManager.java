package bigBang.module.mainModule.client.notifications;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.NewNotificationEventHandler;
import bigBang.module.mainModule.client.userInterface.InfoPanel;
import bigBang.module.mainModule.client.userInterface.InfoPanel.InfoPanelType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public class NotificationsManager {
	
	private com.google.gwt.event.shared.HandlerRegistration newNotificationHandlerRegistration = null;

	/*
	 * TRAY NOTIFICATIONS START
	 */
	
	public void showTrayNotification(final Notification notification){
		if(notification.getCaption() != null || notification.getContent() != null) {
			InfoPanel info = new InfoPanel(notification.getCaption(), notification.getContent(), new ClickHandler() {
				
				public void onClick(ClickEvent event) {
					if(notification.getEvent() != null)
						EventBus.getInstance().fireEvent(notification.getEvent());
				}
			}); 
			InfoPanel.show(InfoPanelType.TRAY_NOTIFICATION, info);
			return;
		}
		
		InfoPanel info = new InfoPanel(new Button("here"), new ClickHandler() {
		
			public void onClick(ClickEvent event) {
				if(notification.getEvent() != null)
					EventBus.getInstance().fireEvent(notification.getEvent());
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
				this.newNotificationHandlerRegistration = EventBus.getInstance().addHandler(NewNotificationEvent.TYPE, new NewNotificationEventHandler() {
					
					public void onNewNotification(NewNotificationEvent event) {
						Notification notification = event.getNotification();
						switch(event.getTYPE()){
						case ALERT_NOTIFICATION:
							MessageBox.error(notification.getCaption(), notification.getContent());
							break;
						case INFO_NOTIFICATION:
							MessageBox.info(notification.getCaption(), notification.getContent());
							break;
						case WARNING_NOTIFICATION:
							MessageBox.alert(notification.getCaption(), notification.getContent());
							break;
						case TRAY_NOTIFICATION:
							showTrayNotification(event.getNotification());
							break;
						}
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
