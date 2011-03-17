package bigBang.library.shared;

import bigBang.library.shared.userInterface.presenter.ViewPresenter;

public interface LoginModule extends Module {

	public String login(String username, String Password);
		
	public String logout();
	
	public boolean isSessionActive();
	
	public String changePassword(String oldPassword, String newPassword);
	
	public ViewPresenter getLoginViewPresenter();
	
	public String setDomain(Domain domain);
}
