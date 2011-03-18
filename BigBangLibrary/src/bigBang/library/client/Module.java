package bigBang.library.client;

import bigBang.library.client.userInterface.presenter.SectionViewPresenter;

public interface Module {

	public void initialize(EventBus eventBus); //Initializes the module
	
	public boolean isInitialized();
	
	public SectionViewPresenter[] getMainMenuSectionPresenters();

}
