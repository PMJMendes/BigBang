package bigBang.module.tasksModule.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface NumberOfTasksUpdateEventHandler extends EventHandler {
	
	public void onUpdate(NumberOfTasksUpdateEvent event);
		
}
