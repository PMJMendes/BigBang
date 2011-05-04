package bigBang.module.tasksModule.client;

import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.ScreenInvokedEvent;
import bigBang.module.tasksModule.client.event.NumberOfTasksUpdateEvent;

import com.google.gwt.user.client.Timer;

public class TasksNotificationsManager extends Timer {
	
	final int DELAY = 1000 * 60; //miliseconds
	private EventBus eventBus;

	public TasksNotificationsManager(EventBus eventBus){
		this.eventBus = eventBus;
	}

	public void run() {
		//TODO Check if there are any new notifications
		Notification notification = new Notification("Notificacao Tasks", "conteudo tasks", new ScreenInvokedEvent(null, TasksSection.ID, null));
		this.eventBus.fireEvent(new NewNotificationEvent(notification,
				Notification.TYPE.TRAY_NOTIFICATION));
		
		
		
		this.eventBus.fireEvent(new NumberOfTasksUpdateEvent((int)(Math.random()*100)));

		this.schedule(this.DELAY);
	}	
}
