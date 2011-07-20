package bigBang.library.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;

import bigBang.library.client.response.ResponseHandler;
import bigBang.library.interfaces.TipifiedListService;
import bigBang.library.interfaces.TipifiedListServiceAsync;
import bigBang.library.shared.TipifiedListItem;

/**
 * A TypifiedListBroker implementation for the BigBang project
 * 
 * @author Francisco Cabrita @ Premium Minds Lda.
 * @see TypifiedListBroker
 */
public class BigBangTypifiedListBroker implements TypifiedListBroker {
	
	public static class Util {
		private static TypifiedListBroker instance;

		public static TypifiedListBroker getInstance(){
			if (instance == null) {
				instance = GWT.create(BigBangTypifiedListBroker.class);
			}
			return instance;
		}
	}
	
	protected final Integer NO_DATA_VERSION = new Integer(0); 
	
	protected TipifiedListServiceAsync service;
	protected Map<String, ArrayList<TypifiedListClient>> clients;
	protected Map<String, Integer> dataVersions;
	protected Map<String, ArrayList<TipifiedListItem>> lists;
	
	/**
	 * The class constructor
	 */
	public BigBangTypifiedListBroker(){
		this.service = TipifiedListService.Util.getInstance();
		this.clients = new HashMap<String, ArrayList<TypifiedListClient>>();
		this.dataVersions = new HashMap<String, Integer>();
		this.lists = new HashMap<String, ArrayList<TipifiedListItem>>();
	}

	@Override
	public void registerClient(String listId, TypifiedListClient client) {
		if(!this.clients.containsKey(listId)){
			this.dataVersions.put(listId, NO_DATA_VERSION);
			this.lists.put(listId, new ArrayList<TipifiedListItem>());
			GWT.log("register list : " + listId);
			refreshListData(listId);
			this.clients.put(listId, new ArrayList<TypifiedListClient>());
		}
		
		List<TypifiedListClient> clientList = this.clients.get(listId);
		
		Integer listDataVersion = getListDataVersion(listId);
		
		if(!clientList.contains(client))
			clientList.add(client);
		client.setTypifiedListItems(this.getListItems(listId));		
		client.setTypifiedDataVersionNumber(listDataVersion.intValue());
	}

	@Override
	public void refreshListData(final String listId) {
		this.service.getListItems(listId, new BigBangAsyncCallback<TipifiedListItem[]>() {

			@Override
			public void onSuccess(TipifiedListItem[] result) {
				GWT.log("recebeu" + listId);
				BigBangTypifiedListBroker.this.lists.put(listId, new ArrayList<TipifiedListItem>(Arrays.asList(result)));
				incrementListDataVersion(listId);
				updateListClients(listId);
			}
		});
	}

	@Override
	public List<TipifiedListItem> getListItems(String listId) {
		if(!this.lists.containsKey(listId))
			throw new RuntimeException("There if no list registered with id \"" + listId + "\"");
		return this.lists.get(listId);
	}	

	@Override
	public void createListItem(final String listId, TipifiedListItem item, final ResponseHandler<TipifiedListItem> handler) {
		
		this.service.createListItem(listId, item, new BigBangAsyncCallback<TipifiedListItem>() {

			@Override
			public void onSuccess(TipifiedListItem result) {
				updateListClients(listId);
				BigBangTypifiedListBroker.this.lists.get(listId).add(result);
				Integer version = BigBangTypifiedListBroker.this.incrementListDataVersion(listId);
				
				for(TypifiedListClient client : BigBangTypifiedListBroker.this.clients.get(listId)) {
					client.addItem(result);
					client.setTypifiedDataVersionNumber(version.intValue());
				}
				
				handler.onResponse(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.onError(null);
				super.onFailure(caught);
			}
		});
	}

	@Override
	public void removeListItem(final String listId, final String itemId, final ResponseHandler<TipifiedListItem> handler) {
		this.service.deleteListItem(listId, itemId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				TipifiedListItem item = null;
				updateListClients(listId);
				
				for(TipifiedListItem i : BigBangTypifiedListBroker.this.lists.get(listId)) {
					if(i.id.equals(itemId)){
						item = i;
						BigBangTypifiedListBroker.this.lists.remove(i);
						break;
					}
				}
				
				if(item == null)
					throw new RuntimeException("The typified list item with id \"" + itemId + "\" does no exist in list with id \"" + listId + "\"");

				Integer version = BigBangTypifiedListBroker.this.incrementListDataVersion(listId);
				
				for(TypifiedListClient client : BigBangTypifiedListBroker.this.clients.get(listId)) {
					client.removeItem(item);
					client.setTypifiedDataVersionNumber(version.intValue());
				}
				handler.onResponse(item);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handler.onError(null);
				super.onFailure(caught);
			}

		});
	}

	@Override
	public void saveListItem(final String listId, final TipifiedListItem item, final ResponseHandler<TipifiedListItem> handler) {
		this.service.saveListItem(listId, item, new BigBangAsyncCallback<TipifiedListItem>() {

			@Override
			public void onSuccess(TipifiedListItem result) {
				updateListClients(listId);
				
				BigBangTypifiedListBroker.this.lists.get(listId).remove(item);
				BigBangTypifiedListBroker.this.lists.get(listId).add(result);
				Integer version = BigBangTypifiedListBroker.this.incrementListDataVersion(listId);

				for(TypifiedListClient client : BigBangTypifiedListBroker.this.clients.get(listId)) {
					client.updateItem(result);
					client.setTypifiedDataVersionNumber(version.intValue());
				}
				handler.onResponse(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handler.onError(null);
				super.onFailure(caught);
			}
		});
	}
	
	/**
	 * Gets the current data version number for the list with the given id
	 * @param listId the id of the list
	 * @return the version of the list data
	 */
	protected Integer getListDataVersion(String listId) {
		if(!this.lists.containsKey(listId))
			throw new RuntimeException("There is no list registered with id \"" + listId + "\"");
		return this.dataVersions.get(listId);
	}
	
	/**
	 * Sets the new data version for the list with the given id
	 * @param listId the id of the list
	 * @param version the new version
	 */
	protected void setListDataVersion(String listId, Integer version) {
		if(!this.lists.containsKey(listId))
			throw new RuntimeException("There if no list registered with id \"" + listId + "\"");
		this.dataVersions.put(listId, version);
	}
	
	/**
	 * Increments the data version for the list with the given id
	 * @param listId the id of the list
	 * @return the new data version
	 */
	protected Integer incrementListDataVersion(String listId) {
		if(!this.lists.containsKey(listId))
			throw new RuntimeException("There if no list registered with id \"" + listId + "\"");
		
		Integer dataVersion = new Integer(this.dataVersions.get(listId).intValue() + 1);
		setListDataVersion(listId, dataVersion);
		return dataVersion;
	}
	
	/**
	 * Updates all the clients for a given list with the latest data
	 * @param listId the id of the list for which clients will be updated
	 */
	protected void updateListClients(String listId){
		if(!this.lists.containsKey(listId))
			throw new RuntimeException("There if no list registered with id \"" + listId + "\"");

		List<TypifiedListClient> listClients = this.clients.get(listId);
		
		for(TypifiedListClient client : listClients) {
			this.updateListClient(listId, client);
		}
	}
	
	/**
	 * Updates one client for a given list with the latest data
	 * @param listId the id of the list for which the client will be updated 
	 * @param client the client to update
	 */
	protected void updateListClient(String listId, TypifiedListClient client) {
		List<TipifiedListItem> items = this.lists.get(listId);
		int currentDataVersion = this.dataVersions.get(listId).intValue();		

		if(!this.clients.get(listId).contains(client))
			throw new RuntimeException("The typified list client is not registered for the list with id : " + listId);
		
		int clientVersion = client.getTypifiedDataVersionNumber();
		
		if(clientVersion > currentDataVersion || clientVersion < this.NO_DATA_VERSION)
			throw new RuntimeException("Unexpected exception. The client has an inconsistent version number " + clientVersion +
					". Expected between "  + this.NO_DATA_VERSION + " and " + currentDataVersion + ".");
		
		if(client.getTypifiedDataVersionNumber() < currentDataVersion){
			client.setTypifiedListItems(items);
			client.setTypifiedDataVersionNumber(currentDataVersion);
		}
	}

}
