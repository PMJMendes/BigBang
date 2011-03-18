package bigBang.module.tasksModule.client;

import com.google.gwt.core.client.GWT;

import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.userInterface.TextBadge;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.tasksModule.client.event.NumberOfTasksUpdateEvent;
import bigBang.module.tasksModule.client.event.NumberOfTasksUpdateEventHandler;
import bigBang.module.tasksModule.client.userInterface.presenter.TasksSectionViewPresenter;
import bigBang.module.tasksModule.client.userInterface.view.TasksSectionView;
import bigBang.module.tasksModule.interfaces.TasksService;
import bigBang.module.tasksModule.interfaces.TasksServiceAsync;

public class TasksModule implements Module {

	private SectionViewPresenter[] mainMenuSectionPresenters;
	
	public void initialize(EventBus eventBus) {
		mainMenuSectionPresenters = new SectionViewPresenter[1];
		
		TasksSection section = new TasksSection();
		TasksSectionView view = new TasksSectionView(); 
		TasksServiceAsync service = GWT.create(TasksService.class);
		TasksSectionViewPresenter presenter = new TasksSectionViewPresenter(eventBus, service, view);
		presenter.setSection(section);
		
		mainMenuSectionPresenters[0] = (SectionViewPresenter)presenter;
		
		new TasksNotificationsManager(eventBus).run();
		
		final TextBadge badge = section.getBadge();
		
		eventBus.addHandler(NumberOfTasksUpdateEvent.TYPE, new NumberOfTasksUpdateEventHandler() {
			
			public void onUpdate(NumberOfTasksUpdateEvent event) {
				badge.setText(event.getValue() + "");
			}
		});
	}

	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	public SectionViewPresenter[] getMainMenuSectionPresenters() {
		return mainMenuSectionPresenters;
	}

}
