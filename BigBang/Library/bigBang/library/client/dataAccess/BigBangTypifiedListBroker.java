package bigBang.library.client.dataAccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.gwt.core.client.GWT;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.interfaces.DependentItemSubServiceAsync;
import bigBang.library.interfaces.TipifiedListService;
import bigBang.library.interfaces.TipifiedListServiceAsync;

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

	protected static final String DEPENDENCY_DELIMITER = "/";

	protected final Integer NO_DATA_VERSION = new Integer(0); 

	protected DependentItemSubServiceAsync service;
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
		if(!validListId(listId)){
			return;
		}

		if(!this.clients.containsKey(listId)){
			this.dataVersions.put(listId, NO_DATA_VERSION);
			this.lists.put(listId, new ArrayList<TipifiedListItem>());

			refreshListData(listId);
			this.clients.put(listId, new ArrayList<TypifiedListClient>());
		}

		List<TypifiedListClient> clientList = this.clients.get(listId);

		Integer listDataVersion = getListDataVersion(listId);

		if(!isClientRegistered(listId, client))
			clientList.add(client);
		client.setTypifiedListItems(this.getListItems(listId));		
		client.setTypifiedDataVersionNumber(listDataVersion.intValue());
	}

	protected boolean validListId(String listId) {
		if(listId == null){
			GWT.log("The list id is null");
			return false;
		}
		if(listId.isEmpty()) {
			GWT.log("The list id is empty");
			return false;
		}
		StringTokenizer tokenizer = new StringTokenizer(listId, DEPENDENCY_DELIMITER);
		if(!tokenizer.hasMoreTokens()){
			return false;
		}
		final String effectiveListId = tokenizer.nextToken();
		if(effectiveListId == null){
			GWT.log("The effective list id is null");
			return false;
		}
		if(tokenizer.hasMoreTokens()){
			String dependencyListId = tokenizer.nextToken();
			if(dependencyListId == null || dependencyListId.isEmpty()){
				return false;
			}
		}else if(listId.contains(DEPENDENCY_DELIMITER)){
			return false;
		}
		return true;
	}

	@Override
	public void unregisterClient(String listId, TypifiedListClient client) {
		if(!validListId(listId)) {
			return;
		}
		if(clients.containsKey(listId)){
			List<TypifiedListClient> clientList = clients.get(listId);
			clientList.remove(client);
			if(clientList.isEmpty()){
				clients.remove(listId);
				dataVersions.remove(listId);
				lists.remove(listId);
			}
		}
	}

	@Override
	public boolean isClientRegistered(String listId, TypifiedListClient client) {
		if(!validListId(listId)) {
			return false;
		}
		if(clients.containsKey(listId)){
			List<TypifiedListClient> clientList = clients.get(listId);
			return clientList.contains(client);
		}
		return false;
	}

	@Override
	public void refreshListData(final String listId) {
		if(!validListId(listId)){
			return;
		}
		StringTokenizer tokenizer = new StringTokenizer(listId, DEPENDENCY_DELIMITER);
		final String effectiveListId = tokenizer.nextToken();
		if(tokenizer.hasMoreTokens()){
			String dependencyListId = tokenizer.nextToken();
			service.getListItemsFilter(effectiveListId, dependencyListId, new BigBangAsyncCallback<TipifiedListItem[]>() {

				@Override
				public void onResponseSuccess(TipifiedListItem[] result) {
					lists.put(listId, new ArrayList<TipifiedListItem>(Arrays.asList(result)));
					incrementListDataVersion(listId);
					updateListClients(listId);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					// TODO Auto-generated method stub
					super.onResponseFailure(caught);
				}
			});
		}else{
			((TipifiedListServiceAsync) this.service).getListItems(effectiveListId, new BigBangAsyncCallback<TipifiedListItem[]>() {

				@Override
				public void onResponseSuccess(TipifiedListItem[] result) {
					lists.put(effectiveListId, new ArrayList<TipifiedListItem>(Arrays.asList(result)));
					incrementListDataVersion(effectiveListId);
					updateListClients(effectiveListId);
				}
			});
		}
	}

	@Override
	public List<TipifiedListItem> getListItems(String listId) {
		if(!this.lists.containsKey(listId))
			throw new RuntimeException("There if no list registered with id \"" + listId + "\"");
		return this.lists.get(listId);
	}

	@Override
	public void getListItems(final String listId, final ResponseHandler<Collection<TipifiedListItem>> handler) {
		if(!validListId(listId)){
			handler.onError(new String[]{
					new String("The given list id is invalid : " + listId)	
			});
			return;
		}
		StringTokenizer tokenizer = new StringTokenizer(listId, DEPENDENCY_DELIMITER);
		final String effectiveListId = tokenizer.nextToken();
		if(tokenizer.hasMoreTokens()){
			String dependencyListId = tokenizer.nextToken();
			service.getListItemsFilter(effectiveListId, dependencyListId, new BigBangAsyncCallback<TipifiedListItem[]>() {

				@Override
				public void onResponseSuccess(TipifiedListItem[] result) {
					handler.onResponse(new ArrayList<TipifiedListItem>(Arrays.asList(result)));
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the list items for list " + listId)	
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			((TipifiedListServiceAsync) this.service).getListItems(effectiveListId, new BigBangAsyncCallback<TipifiedListItem[]>() {

				@Override
				public void onResponseSuccess(TipifiedListItem[] result) {
					handler.onResponse(new ArrayList<TipifiedListItem>(Arrays.asList(result)));
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the list items for list " + listId)	
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public void getListItem(String listId, String itemId, final ResponseHandler<TipifiedListItem> handler) {
		((TipifiedListServiceAsync) this.service).getSingleItem(listId, itemId, new BigBangAsyncCallback<TipifiedListItem>() {

			@Override
			public void onResponseSuccess(TipifiedListItem result) {
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the listItem")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createListItem(final String listId, TipifiedListItem item, final ResponseHandler<TipifiedListItem> handler) {
		if(!validListId(listId)) {
			return;
		}

		if(isCompositeId(listId)) {
			StringTokenizer tokenizer = new StringTokenizer(listId, DEPENDENCY_DELIMITER);
			String effectiveListId = tokenizer.nextToken();
			String dependencyId = tokenizer.nextToken();

			((TipifiedListServiceAsync) this.service).createListItemFiltered(effectiveListId, dependencyId, item, new BigBangAsyncCallback<TipifiedListItem>() {

				@Override
				public void onResponseSuccess(TipifiedListItem result) {
					updateListClients(listId);
					lists.get(listId).add(result);
					Integer version = incrementListDataVersion(listId);

					for(TypifiedListClient client : clients.get(listId)) {
						client.addItem(result);
						client.setTypifiedDataVersionNumber(version.intValue());
					}

					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{"The item could not be created"});
					super.onResponseFailure(caught);
				}
			});
		}else{
			((TipifiedListServiceAsync) this.service).createListItem(listId, item, new BigBangAsyncCallback<TipifiedListItem>() {

				@Override
				public void onResponseSuccess(TipifiedListItem result) {
					updateListClients(listId);
					lists.get(listId).add(result);
					Integer version = incrementListDataVersion(listId);

					for(TypifiedListClient client : clients.get(listId)) {
						client.addItem(result);
						client.setTypifiedDataVersionNumber(version.intValue());
					}

					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{"The item could not be created"});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public void removeListItem(final String listId, final String itemId, final ResponseHandler<TipifiedListItem> handler) {
		if(!validListId(listId)) {
			return;
		}
		String effectiveListId = listId;
		if(isCompositeId(listId)){
			StringTokenizer tokenizer = new StringTokenizer(listId, DEPENDENCY_DELIMITER);
			effectiveListId = tokenizer.nextToken();
		}

		((TipifiedListServiceAsync) this.service).deleteListItem(effectiveListId, itemId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				TipifiedListItem item = null;

				for(TipifiedListItem i : lists.get(listId)) {
					if(i.id.equalsIgnoreCase(itemId)){
						item = i;
						lists.get(listId).remove(i);
						break;
					}
				}

				if(item == null)
					throw new RuntimeException("The typified list item with id \"" + itemId + "\" does no exist in list with id \"" + listId + "\"");

				Integer version = incrementListDataVersion(listId);

				for(TypifiedListClient client : clients.get(listId)) {
					client.removeItem(item);
					client.setTypifiedDataVersionNumber(version.intValue());
				}
				handler.onResponse(item);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{"The item could not be removed"});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void saveListItem(final String listId, final TipifiedListItem item, final ResponseHandler<TipifiedListItem> handler) {
		if(!validListId(listId)) {
			return;
		}

		((TipifiedListServiceAsync) this.service).saveListItem(listId, item, new BigBangAsyncCallback<TipifiedListItem>() {

			@Override
			public void onResponseSuccess(TipifiedListItem result) {
				updateListClients(listId);

				lists.get(listId).remove(item);
				lists.get(listId).add(result);
				Integer version = incrementListDataVersion(listId);

				for(TypifiedListClient client : clients.get(listId)) {
					client.updateItem(result);
					client.setTypifiedDataVersionNumber(version.intValue());
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{"The item could not be saved"});
				super.onResponseFailure(caught);
			}
		});
	}

	/**
	 * Gets the current data version number for the list with the given id
	 * @param listId the id of the list
	 * @return the version of the list data
	 */
	protected Integer getListDataVersion(String listId) {
		if(!validListId(listId)) {
			return NO_DATA_VERSION;
		}

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
		if(!validListId(listId)) {
			return;
		}
		if(listId == null || !this.lists.containsKey(listId))
			throw new RuntimeException("There if no list registered with id \"" + listId + "\"");
		if(version == null || version < NO_DATA_VERSION){
			throw new RuntimeException("The version is not valid");
		}
		this.dataVersions.put(listId, version);
	}

	/**
	 * Increments the data version for the list with the given id
	 * @param listId the id of the list
	 * @return the new data version
	 */
	protected Integer incrementListDataVersion(String listId) {
		if(!validListId(listId)) {
			return NO_DATA_VERSION;
		}

		if(!this.lists.containsKey(listId))
			throw new RuntimeException("There if no list registered with id \"" + listId + "\"");

		Integer currentVersionNumber = this.dataVersions.get(listId);
		currentVersionNumber = currentVersionNumber == null ? 0 : currentVersionNumber;
		Integer dataVersion = new Integer(currentVersionNumber.intValue() + 1);
		setListDataVersion(listId, dataVersion);
		return dataVersion;
	}

	/**
	 * Updates all the clients for a given list with the latest data
	 * @param listId the id of the list for which clients will be updated
	 */
	protected void updateListClients(String listId){
		if(!validListId(listId)) {
			return;
		}

		if(!this.lists.containsKey(listId))
			throw new RuntimeException("There if no list registered with id \"" + listId + "\"");

		List<TypifiedListClient> listClients = this.clients.get(listId);

		if(listClients != null){
			for(TypifiedListClient client : listClients) {
				this.updateListClient(listId, client);
			}
		}else{
			this.clients.remove(listId);
		}
	}

	/**
	 * Updates one client for a given list with the latest data
	 * @param listId the id of the list for which the client will be updated 
	 * @param client the client to update
	 */
	protected void updateListClient(String listId, TypifiedListClient client) {
		if(!validListId(listId)) {
			return;
		}

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

	/**
	 * Returns true if the given id represents a list with a dependency
	 * @param listId The id of the list
	 * @return true if the id is composite or false otherwise.
	 */
	protected boolean isCompositeId(String listId) {
		if(!validListId(listId)){
			throw new RuntimeException("The listId is invalid");
		}
		StringTokenizer tokenizer = new StringTokenizer(listId, DEPENDENCY_DELIMITER);
		return tokenizer.countTokens() > 1;
	}

}
