package bigBang.library.client;

import bigBang.library.client.dataAccess.DataBroker;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;

public interface Module {

	public void initialize(EventBus eventBus); //Initializes the module
	
	public boolean isInitialized();
	
	public SectionViewPresenter[] getMainMenuSectionPresenters();

	public void initialize(EventBus eventBus,
			BigBangPermissionManager permissionManager);
	
	/**
	 * Gets the broker implementations defined in the module
	 * @return An array of data broker instances
	 */
	public DataBroker<?>[] getBrokerImplementations();
	
	/**
	 * Gets the broker dependencies required by this module
	 * @return An array of data element ids for which brokers are required
	 */
	public String[] getBrokerDependencies();

}
