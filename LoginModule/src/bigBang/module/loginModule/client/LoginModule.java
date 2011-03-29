package bigBang.module.loginModule.client;

import com.google.gwt.core.client.GWT;

import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.Domain;
import bigBang.library.client.EventBus;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.interfaces.Service;
import bigBang.module.loginModule.client.userInterface.presenter.LoginViewPresenter;
import bigBang.module.loginModule.client.userInterface.view.LoginView;
import bigBang.module.loginModule.interfaces.AuthenticationService;

public class LoginModule implements bigBang.library.client.LoginModule {

	private EventBus eventBus;
	
	public void initialize(EventBus eventBus, BigBangPermissionManager permissionManager) {
		this.eventBus = eventBus;
	}
	
	public void initialize(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	public String login(String username, String Password) {
		// TODO Auto-generated method stub
		return null;
	}

	public String logout() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSessionActive() {
		// TODO Auto-generated method stub
		return false;
	}

	public String changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		return null;
	}

	public String setDomain(Domain domain) {
		// TODO Auto-generated method stub
		return null;
	}

	public ViewPresenter getLoginViewPresenter() {
		//return null;
		LoginViewPresenter presenter = new LoginViewPresenter(eventBus, new LoginView());
		presenter.setService((Service) GWT.create(AuthenticationService.class));
		return presenter;
	}

	public SectionViewPresenter[] getMainMenuSectionPresenters() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
