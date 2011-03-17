package bigBang.module.loginModule.client;

import bigBang.library.shared.Domain;
import bigBang.library.shared.EventBus;
import bigBang.library.shared.Service;
import bigBang.library.shared.userInterface.MenuSection;
import bigBang.library.shared.userInterface.presenter.SectionViewPresenter;
import bigBang.library.shared.userInterface.presenter.ViewPresenter;
import bigBang.module.loginModule.client.userInterface.presenter.LoginViewPresenter;
import bigBang.module.loginModule.client.userInterface.view.LoginView;
import bigBang.module.loginModule.shared.AuthenticationService;

import com.google.gwt.core.client.GWT;

public class LoginModule implements bigBang.library.shared.LoginModule {

	public void initialize(EventBus eventBus) {
		// TODO Auto-generated method stub
		
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
		return null;
		/*
		LoginViewPresenter presenter = new LoginViewPresenter(null, new LoginView());
		presenter.setService((Service) GWT.create(AuthenticationService.class));
		return presenter;
		*/
	}

	public SectionViewPresenter[] getMainMenuSectionPresenters() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
