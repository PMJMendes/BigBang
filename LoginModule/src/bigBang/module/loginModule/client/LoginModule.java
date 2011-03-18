package bigBang.module.loginModule.client;

import bigBang.library.client.Domain;
import bigBang.library.client.EventBus;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class LoginModule implements bigBang.library.client.LoginModule {

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
