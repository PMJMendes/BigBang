package bigBang.module.mainModule.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

import bigBang.library.client.EventBus;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;


public class MainModule implements bigBang.library.client.MainModule {

	private static EventBus eventBus; //Where app events are posted
	private static ApplicationController applicationController; //Top level control of the application
	private static HistoryManager historyManager; //Manages the application history
	private static ProcessManager processManager; //Manages the processes and their respective operations

	public MainModule() {
	}

	public void initialize(EventBus eventBus) {
		MainModule.eventBus = eventBus;
		historyManager = GWT.create(HistoryManager.class);
		processManager = GWT.create(ProcessManager.class);
		applicationController = new ApplicationController(eventBus, historyManager, processManager);
	}

	public boolean isInitialized() {
		return applicationController != null;
	}

	public void run() {
		applicationController.go(RootPanel.get());
	}

	public void setLoginPresenter(ViewPresenter loginViewPresenter) {
		applicationController.setLoginViewPresenter(loginViewPresenter);
	}

	public SectionViewPresenter[] getMainMenuSectionPresenters() {
		return null;
	}

	public void includeMainMenuSectionPresenters(SectionViewPresenter[] sectionPresenters) {
		if(sectionPresenters == null)
			return;
		for(int i = 0; i < sectionPresenters.length; i++)
			this.includeMainMenuSectionPresenter(sectionPresenters[i]);
	}

	public void includeMainMenuSectionPresenter(SectionViewPresenter section) {
		applicationController.includeMainMenuSectionPresenter(section);
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}

}