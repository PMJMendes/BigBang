package bigBang.module.mainModule.client;

import bigBang.library.client.EventBus;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.event.LoginSuccessEvent;
import bigBang.library.client.event.LoginSuccessEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.mainModule.client.notifications.NotificationsManager;
import bigBang.module.mainModule.client.userInterface.MainViewController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

public class ApplicationController {

	private NotificationsManager notificationsManager;
	private HasWidgets mainContainer;

	public ApplicationController() {
		notificationsManager = (NotificationsManager) GWT.create(NotificationsManager.class);
		this.bindToEvents();
	}
	
	public void startNotificationsManager(){
		notificationsManager.enableTrayNotifications();
	}
	
	private void bindToEvents(){
		EventBus.getInstance().addHandler(LoginSuccessEvent.TYPE, new LoginSuccessEventHandler() {
			public void onLoginSuccess(LoginSuccessEvent event) {
				showMainScreen();
			}
		});
	}

	public void go() {
		RootPanel.get().setSize("100%", Window.getClientHeight()+"px");
		RootPanel.get().getElement().getStyle().setOverflow(Overflow.HIDDEN);
		Window.addResizeHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				RootPanel.get().setSize("100%", Window.getClientHeight()+"px");
			}
		});
		this.mainContainer = RootPanel.get();
		showLoginScreen();
	}

	public void showMainScreen(){
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {
//				ViewPresenter mainScreenPresenter = ViewPresenterFactory.getInstance().getViewPresenter("MAIN_SCREEN");
//				mainScreenPresenter.go(ApplicationController.this.mainContainer);
				new MainViewController(mainContainer);
				startNotificationsManager();
			}
			
			@Override
			public void onFailure(Throwable reason) {
				GWT.log("Could not present the Application Interface : " + reason.getMessage());
			}
		});
	}

	public void showLoginScreen(){
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ViewPresenter loginViewPresenter = ViewPresenterFactory.getInstance().getViewPresenter("LOGIN");
				loginViewPresenter.go(ApplicationController.this.mainContainer);
			}

			public void onFailure(Throwable reason) {
				GWT.log("Could not present the login module : " + reason.getMessage());
			}
		});
	}

}
