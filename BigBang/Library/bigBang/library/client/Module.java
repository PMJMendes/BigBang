package bigBang.library.client;

import bigBang.definitions.client.dataAccess.DataBroker;

public interface Module {

	public void initialize(); //Initializes the module
	
	public boolean isInitialized();
	
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
