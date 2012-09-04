package bigBang.module.mainModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.mainModule.client.userInterface.presenter.MainScreenViewPresenter;
import bigBang.module.mainModule.client.userInterface.view.MainScreenView;

import com.google.gwt.core.client.GWT;


public class MainModule implements bigBang.library.client.MainModule {

	private ApplicationController applicationController; //Top level control of the application
	private boolean initialized = false;

	public void initialize() {
		this.applicationController = (ApplicationController) GWT.create(ApplicationController.class);
		registerViewPresenters();
		this.initialized = true;
	}

	public boolean isInitialized() {
		return this.initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MAIN_SCREEN", new ViewPresenterInstantiator() {
			
			@Override
			public ViewPresenter getInstance() {
				MainScreenView view =  (MainScreenView) GWT.create(MainScreenView.class);
				MainScreenViewPresenter presenter = new MainScreenViewPresenter(view);
				return presenter;
			}
		});
	}
	
	public void run() {
		applicationController.go();
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