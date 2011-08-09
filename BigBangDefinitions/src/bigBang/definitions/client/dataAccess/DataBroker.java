package bigBang.definitions.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An abstract implementation of a data broker for a given data element type.
 * Data brokers should implement a caching mechanism and have the duty to propagate any changes to their clients.
 * @param <T> The type of the data element to be handled by the data broker.
 */
public abstract class DataBroker<T> implements DataBrokerInterface<T> {
	
	protected String dataElementId;
	protected int dataVersion;
	protected DataBrokerCache cache;
	private List<DataBrokerClient<T>> clients;
	
	protected final int INITIAL_DATA_VERSION = 0;
	
	/**
	 * The constructor
	 */
	public DataBroker(){
		this(DataBrokerCache.Mechanism.LRU);
	}
	
	public DataBroker(DataBrokerCache.Mechanism cacheMechanism) {
		this.dataVersion = INITIAL_DATA_VERSION;
		this.cache = new DataBrokerCache(cacheMechanism);
		this.clients = new ArrayList<DataBrokerClient<T>>();
	}
	
	@Override
	public void incrementDataVersion(){
		this.dataVersion++;
	}
	
	@Override
	public boolean checkClientDataVersions(){
		for(DataBrokerClient<T> c : clients) {
			if(c.getDataVersion(this.dataElementId) != getCurrentDataVersion()){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int getCurrentDataVersion(){
		return this.dataVersion; 
	}

	@Override
	public void registerClient(DataBrokerClient<T> client) {
		if(!this.clients.contains(client)){
			this.clients.add(client);
			synchronizeClient(client);			
		}
	}

	@Override
	public void unregisterClient(DataBrokerClient<T> client) {
		this.clients.remove(client);
	}
	
	@Override
	public Collection<DataBrokerClient<T>> getClients() {
		return this.clients;
	}

	/**
	 * Synchronizes a client to the most up-to-date version of the known data.
	 * @param client The client to be synchronized
	 */
	protected void synchronizeClient(DataBrokerClient<T> client) {
		 client.setDataVersionNumber(this.dataElementId, this.dataVersion);
	}

	@Override
	public String getDataElementId() {
		return this.dataElementId;
	}
	
}
