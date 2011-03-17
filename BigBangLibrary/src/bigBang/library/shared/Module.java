package bigBang.library.shared;

import bigBang.library.shared.userInterface.presenter.SectionViewPresenter;

public interface Module {

	public void initialize(EventBus eventBus); //Initializes the module
	
	public boolean isInitialized();
	
	public SectionViewPresenter[] getMainMenuSectionPresenters();

}
