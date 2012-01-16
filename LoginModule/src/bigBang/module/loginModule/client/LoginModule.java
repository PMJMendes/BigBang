package bigBang.module.loginModule.client;

import com.google.gwt.core.client.GWT;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.loginModule.client.userInterface.presenter.LoginViewPresenter;
import bigBang.module.loginModule.client.userInterface.view.LoginView;

public class LoginModule implements Module {

	private boolean initialized = false;
	
	@Override
	public void initialize() {
		registerViewPresenters();
		this.initialized = true;
	}

	@Override
	public boolean isInitialized() {
		return this.initialized;
	}
	
	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("LOGIN", new ViewPresenterInstantiator() {
			
			@Override
			public ViewPresenter getInstance() {
				LoginView view = (LoginView) GWT.create(LoginView.class);
				LoginViewPresenter presenter = new LoginViewPresenter(view);
				return presenter;
			}
		});
	}
	
	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return null;
	}

	@Override
	public String[] getBrokerDependencies() {
		return null;
	}

}
