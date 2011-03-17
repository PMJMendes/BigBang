package bigBang.module.tasksModule.client;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.event.NewNotificationEvent;
import bigBang.library.shared.event.NewNotificationEventHandler;
import bigBang.library.shared.userInterface.MenuSection;
import bigBang.library.shared.userInterface.TextBadge;
import bigBang.library.shared.userInterface.view.View;

public class TasksSection implements MenuSection {

	private static final String ID = "TASKS_SECTION";
	private final String DESCRIPTION = "Agenda";
	private final String SHORT_DESCRIPTION = "Agenda";
	
	private TextBadge badge;
	private View view;
	
	public TasksSection() {
		init();
	}

	private void init() {
		this.badge = new TextBadge("");
	}
	
	public String getId() {
		return ID;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getShortDescription() {
		return SHORT_DESCRIPTION;
	}

	public TextBadge getBadge() {
		return this.badge;
	}

	public boolean hasBadge() {
		return this.badge != null;
	}

	public View getView() {
		return view;
	}
	
	public void registerEventHandlers(EventBus eventBus) {
		
	}

}
