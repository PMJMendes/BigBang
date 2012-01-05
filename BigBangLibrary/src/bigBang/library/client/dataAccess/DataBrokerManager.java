package bigBang.library.client.dataAccess;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;

import com.google.gwt.core.client.GWT;

/**
 * A class that manages broker objects and broker dependencies
 */
public class DataBrokerManager {

	public static class Util {
		private static DataBrokerManager instance;

		public static DataBrokerManager getInstance(){
			if (instance == null) {
				instance = GWT.create(DataBrokerManager.class);
			}
			return instance;
		}
	}
	
	protected Map<String, DataBroker<?>> brokers;

	/**
	 * The constructor
	 */
	public DataBrokerManager(){
		this.brokers = new HashMap<String, DataBroker<?>>();
	}
	
	/**
	 * Registers a broker implementation for a data element with the id given in the argument-
	 * @param dataElementId The id for the data element accessed by the broker implementation
	 * @param broker The broker implementation
	 */
	public void registerBrokerImplementation(String dataElementId, DataBroker<?> broker){
		if(hasBrokerImplementationForDataElement(dataElementId)) {
			GWT.log("Ignoring broker implementation. There is already a broker implementation for the data element with id: " + dataElementId);
		}else{
			brokers.put(dataElementId, broker);
		}
	}
	
	/**
	 * Returns whether or not a broker implemention for a data element is already registered.
	 * @param dataElementId The id of the data element
	 * @return true if a broker implementation is registered, false otherwise
	 */
	public boolean hasBrokerImplementationForDataElement(String dataElementId) {
		return this.brokers.containsKey(dataElementId);
	}
	
	/**
	 * Gets the broker implementation for a given data element
	 * @param dataElementId The id of the data element
	 * @return The broker implementation
	 */
	public DataBroker<?> getBroker(String dataElementId) {
		if(!hasBrokerImplementationForDataElement(dataElementId))
			throw new RuntimeException("The is no broker implementation for the given data element with id: " + dataElementId);
		return brokers.get(dataElementId);
	}
	
	public static DataBroker<?> staticGetBroker(String dataElementId){
		return DataBrokerManager.Util.getInstance().getBroker(dataElementId);
	}
	
}
