package bigBang.module.mainModule.client;

import bigBang.library.client.EventBus;
import bigBang.library.client.event.LoginSuccessEvent;
import bigBang.library.client.event.LoginSuccessEventHandler;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.mainModule.client.notifications.NotificationsManager;
import bigBang.module.mainModule.client.userInterface.presenter.MainScreenViewPresenter;
import bigBang.module.mainModule.client.userInterface.view.MainScreenView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

public class ApplicationController {

	private EventBus eventBus;
	private HistoryManager historyManager;
	private NotificationsManager notificationsManager;
	private ProcessManager processManager;

	private ViewPresenter loginPresenter;
	private MainScreenViewPresenter mainScreenViewPresenter;
	private HasWidgets mainContainer;

	public ApplicationController(final EventBus eventBus, HistoryManager historyManager, ProcessManager processManager) {
		this.eventBus = eventBus;
		this.historyManager = historyManager;
		this.processManager = processManager;
		
		mainScreenViewPresenter = new MainScreenViewPresenter(eventBus, new MainScreenView());
		notificationsManager = new NotificationsManager(eventBus);
		
		this.bindEvents();
	}

	public void startNotificationsManager(){
		notificationsManager.enableTrayNotifications();
	}

	public void setLoginViewPresenter(ViewPresenter loginPresenter){		
		this.loginPresenter = loginPresenter;
		if(this.loginPresenter != null)
			this.loginPresenter.setEventBus(eventBus);
	}

	public void go(HasWidgets mainContainer) {
		this.mainContainer = mainContainer;

		if(loginPresenter != null)
			showLoginScreen();
		else
			showMainScreen();
	}

	public void showMainScreen(){
		RootPanel.get().setSize(Window.getClientWidth() + "px", Window.getClientHeight() + "px");

		mainScreenViewPresenter.go(mainContainer);
		startNotificationsManager();
	}

	public void showLoginScreen(){
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				loginPresenter.go(mainContainer);
			}

			public void onFailure(Throwable reason) {
				GWT.log("Could not present the login module : " + reason.getMessage());
			}
		});
	}

	public void includeMainMenuSectionPresenter(SectionViewPresenter sectionPresenter){
		if(sectionPresenter == null)
			return;
		this.mainScreenViewPresenter.addMenuSectionPresenter(sectionPresenter);
	}

	private void bindEvents(){
		eventBus.addHandler(LoginSuccessEvent.TYPE, new LoginSuccessEventHandler() {
			public void onLoginSuccess(LoginSuccessEvent event) {
				GWT.log("login ok for " + event.getUsername());
				showMainScreen();
			}
		});
	}

}
