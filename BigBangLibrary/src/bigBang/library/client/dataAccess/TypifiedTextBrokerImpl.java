package bigBang.library.client.dataAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anotherbigidea.flash.movie.Shape.SetLineStyle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.interfaces.TypifiedTextService;
import bigBang.library.interfaces.TypifiedTextServiceAsync;

public class TypifiedTextBrokerImpl implements TypifiedTextBroker {

	public static class Util {

		private static TypifiedTextBroker instance;

		public static TypifiedTextBroker getInstance(){
			if(instance == null){
				instance = GWT.create(TypifiedTextBrokerImpl.class);
			}

			return instance;
		}
	}

	protected final Integer NO_DATA_VERSION = new Integer(0);

	protected TypifiedTextServiceAsync service;
	protected Map<String, ArrayList<TypifiedTextClient>> clients;
	protected Map<String, Integer> dataVersions;
	protected Map<String, ArrayList<TypifiedText>> lists;

	/**
	 * The class constructor
	 */


	public TypifiedTextBrokerImpl(){

		this.service = TypifiedTextService.Util.getInstance();
		this.clients = new HashMap<String, ArrayList<TypifiedTextClient>>();
		this.dataVersions = new HashMap<String, Integer>();
		this.lists = new HashMap<String, ArrayList<TypifiedText>>();
	}

	@Override
	public void registerClient(String tag, TypifiedTextClient client) {
		if(!validTag(tag)){
			return;
		}

		if(this.clients.containsKey(tag)){
			this.dataVersions.put(tag, NO_DATA_VERSION);
			this.lists.put(tag, new ArrayList<TypifiedText>());

			refreshListData(tag);
			this.clients.put(tag, new ArrayList<TypifiedTextClient>());
		}

		List<TypifiedTextClient> clientList = this.clients.get(tag);

		Integer listDataVersion = getListDataVersion(tag);

		if(!isClientRegistered(tag, client))
			clientList.add(client);

		client.setTypifiedTexts(this.getTexts(tag));
		client.setTypifiedDataVersionNumber(listDataVersion.intValue());
	}

	private Integer getListDataVersion(String tag) {
		if(!validTag(tag)) {
			return NO_DATA_VERSION;
		}

		if(!this.lists.containsKey(tag))
			throw new RuntimeException("There is no list registered with id \"" + tag + "\"");
		return this.dataVersions.get(tag);
	}

	private boolean validTag(String tag) {

		if(tag == null){
			GWT.log("The list id is null");
			return false;
		}
		if(tag.isEmpty()) {
			GWT.log("The list id is empty");
			return false;
		}

		return true;
	}

	@Override
	public void unregisterClient(String tag, TypifiedTextClient client) {
		if(!validTag(tag)){
			return;
		}
		if(clients.containsKey(tag)){
			List<TypifiedTextClient> clientList = clients.get(tag);
			clientList.remove(client);

			if(clientList.isEmpty()){
				clients.remove(tag);
				dataVersions.remove(tag);
				lists.remove(tag);
			}
		}
	}

	@Override
	public void refreshListData(final String tag) {
		if(!validTag(tag)){
			return;
		}

		this.service.getTexts(tag, new BigBangAsyncCallback<TypifiedText[]>() {

			@Override
			public void onSuccess(TypifiedText[] result) {
				TypifiedTextBrokerImpl.this.lists.put(tag, new ArrayList<TypifiedText>());
				incrementListDataVersion(tag);
				updateListClients(tag);
			}

			@Override
			public void onFailure(Throwable caught){

				super.onFailure(caught);
			}


		});

	}

	protected void updateListClients(String tag) {
		if(!validTag(tag)){
			return;
		}

		if(!this.lists.containsKey(tag)){
			throw new RuntimeException("There if no list registered with id \"" + tag + "\"");
		}

		List<TypifiedTextClient> listClients = this.clients.get(tag);

		for(TypifiedTextClient client : listClients){
			this.updateListClient(tag, client);
		}


	}

	protected void updateListClient(String tag, TypifiedTextClient client) {
		if(!validTag(tag)){
			return;
		}

		List<TypifiedText> texts = this.lists.get(tag);
		int currentDataVersion = this.dataVersions.get(tag).intValue();

		if(!this.clients.get(tag).contains(client))
			throw new RuntimeException("The typified list client is not registered for the list with id : " + tag);

		int clientVersion = client.getTypifiedDataVersionNumber();

		if(clientVersion > currentDataVersion || clientVersion < this.NO_DATA_VERSION)
			throw new RuntimeException("Unexpected exception. The client has an inconsistent version number " + clientVersion +
					". Expected between "  + this.NO_DATA_VERSION + " and " + currentDataVersion + ".");

		if(client.getTypifiedDataVersionNumber() < currentDataVersion){
			client.setTypifiedTexts(texts);
			client.setTypifiedDataVersionNumber(currentDataVersion);			
		}
	}

	protected Integer incrementListDataVersion(String tag) {
		if(!validTag(tag)){
			return NO_DATA_VERSION;
		}

		if(!this.lists.containsKey(tag)){
			throw new RuntimeException("There if no list registered with id \"" + tag + "\"");
		}

		Integer currentVersionNumber = this.dataVersions.get(tag);
		Integer dataVersion = new Integer(currentVersionNumber.intValue() + 1);
		setListDataVersion(tag, dataVersion);
		return dataVersion;

	}

	private void setListDataVersion(String tag, Integer dataVersion) {
		if(!validTag(tag)){
			return;
		}

		if(tag == null || !this.lists.containsKey(tag))
			throw new RuntimeException("There if no list registered with id \"" + tag + "\"");
		if(dataVersion == null || dataVersion < NO_DATA_VERSION){
			throw new RuntimeException("The version is not valid");
		}
		this.dataVersions.put(tag, dataVersion);
	}

	@Override
	public List<TypifiedText> getTexts(String tag) {
		if(!this.lists.containsKey(tag)){
			throw new RuntimeException("There if no list registered with id \"" + tag + "\"");
		}
		return lists.get(tag);
	}

	@Override
	public TypifiedText getText(String tag, String textId) {
		// TODO 
		return null;
	}

	@Override
	public void createText(final String tag, TypifiedText text, final ResponseHandler<TypifiedText> handler) {
		if(!validTag(tag)){
			return;
		}

		this.service.createText(text, new BigBangAsyncCallback<TypifiedText>() {

			@Override
			public void onSuccess(TypifiedText result) {
				updateListClients(tag);
				TypifiedTextBrokerImpl.this.lists.get(tag).add(result);
				Integer version = TypifiedTextBrokerImpl.this.incrementListDataVersion(tag);

				for(TypifiedTextClient client: TypifiedTextBrokerImpl.this.clients.get(tag)){
					client.addText(result);
					client.setTypifiedDataVersionNumber(version.intValue());
				}

				handler.onResponse(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{"The text could not be created"});
				super.onFailure(caught);
			}
		});

	}

	@Override
	public void removeText(final String tag, final String textId, final ResponseHandler<TypifiedText> handler) {
		if(!validTag(tag)){
			return;
		}

		this.service.deleteText(textId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				TypifiedText text = null;

				for(TypifiedText i: TypifiedTextBrokerImpl.this.lists.get(tag)){
					if(i.id.equalsIgnoreCase(textId)){
						text = i;
						TypifiedTextBrokerImpl.this.lists.remove(textId);
						break;
					}
				}

				if(text == null){
					throw new RuntimeException("The typified text with id \"" + textId + "\" does no exist in list with tag \"" + tag + "\"");
				}

				Integer version = TypifiedTextBrokerImpl.this.incrementListDataVersion(tag);

				for(TypifiedTextClient client : TypifiedTextBrokerImpl.this.clients.get(tag)){

					client.removeText(text);
					client.setTypifiedDataVersionNumber(version.intValue());
				}

				handler.onResponse(text);
			}

			@Override
			public void onFailure(Throwable caught){
				handler.onError(new String[]{"The text could not be removed"});
				super.onFailure(caught);
			}
		});
	}

	@Override
	public void saveText(final String tag, final TypifiedText text, final ResponseHandler<TypifiedText> handler) {
		if(!validTag(tag)){
			return;
		}
		
		this.service.saveText(text, new BigBangAsyncCallback<TypifiedText>() {

			@Override
			public void onSuccess(TypifiedText result) {
				updateListClients(tag);
				
				TypifiedTextBrokerImpl.this.lists.get(tag).remove(text);
				TypifiedTextBrokerImpl.this.lists.get(tag).add(result);
				Integer version = TypifiedTextBrokerImpl.this.incrementListDataVersion(tag);
				
				for(TypifiedTextClient client: TypifiedTextBrokerImpl.this.clients.get(tag)){
					client.updateText(result);
					client.setTypifiedDataVersionNumber(version);
				}
				handler.onResponse(result);
			}
			
			@Override
			public void onFailure(Throwable caught){
				handler.onError(new String[]{"The text could not be saved"});
				super.onFailure(caught);
				
			}
		
		
		
		});

	}

	@Override
	public boolean isClientRegistered(String tag, TypifiedTextClient client) {
		if(!validTag(tag)){
			return false;
		}

		if(clients.containsKey(tag)){
			List<TypifiedTextClient> clientList = clients.get(tag);
			return clientList.contains(client);
		}
		return false;
	}

}
