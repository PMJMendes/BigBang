package bigBang.module.tasksModule.client;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.Notification;
import bigBang.library.shared.event.NewNotificationEvent;
import bigBang.module.tasksModule.client.event.NumberOfTasksUpdateEvent;

import com.google.gwt.user.client.Timer;

public class TasksNotificationsManager extends Timer {
	
	final int DELAY = 1000 * 10; //miliseconds
	private EventBus eventBus;

	public TasksNotificationsManager(EventBus eventBus){
		this.eventBus = eventBus;
	}

	public void run() {
		//TODO Check if there are any new notifications
		this.eventBus.fireEvent(new NewNotificationEvent(new Notification("Notificacao Tasks", "conteudo tasks"),
				Notification.TYPE.TRAY_NOTIFICATION));
		
		this.eventBus.fireEvent(new NumberOfTasksUpdateEvent((int)(Math.random()*100)));

		this.schedule(this.DELAY);
	}	
}
