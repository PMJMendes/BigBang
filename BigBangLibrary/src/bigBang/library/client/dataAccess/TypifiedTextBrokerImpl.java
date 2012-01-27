package bigBang.library.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.core.client.GWT;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.interfaces.TypifiedTextService;
import bigBang.library.interfaces.TypifiedTextServiceAsync;

public class TypifiedTextBrokerImpl extends DataBroker<TypifiedText> implements TypifiedTextBroker {

	public static class Util {

		private static TypifiedTextBroker instance;

		public static TypifiedTextBroker getInstance(){
			if(instance == null){
				instance = new TypifiedTextBrokerImpl();
			}

			return instance;
		}
	}

	protected final Integer NO_DATA_VERSION = new Integer(0);

	protected Map<String, Boolean> dataRefreshRequirements;
	protected Map<String, List<TypifiedTextClient>> clients;
	protected Map<String, Integer> dataVersions;
	protected Map<String, List<TypifiedText>> texts;

	protected TypifiedTextServiceAsync service;
	/**
	 * The class constructor
	 */


	public TypifiedTextBrokerImpl(){

		this.dataElementId = BigBangConstants.TypifiedListIds.TYPIFIED_TEXT;
		this.service = TypifiedTextService.Util.getInstance();
		this.clients = new HashMap<String, List<TypifiedTextClient>>();
		this.dataVersions = new HashMap<String, Integer>();
		this.texts = new HashMap<String, List<TypifiedText>>();
		this.dataRefreshRequirements = new HashMap<String, Boolean>();
	}

	@Override
	public void registerClient(final String tag, final TypifiedTextClient client) {

		if(!this.clients.containsKey(tag)){
			List<TypifiedTextClient> clientList = new ArrayList<TypifiedTextClient>();
			clients.put(tag, clientList);
			requireDataRefresh(tag);
			dataVersions.put(tag, NO_DATA_VERSION);
			getTexts(tag, new ResponseHandler<List<TypifiedText>>() {

				@Override
				public void onResponse(List<TypifiedText> response) {
					clients.get(tag).add(client);
					updateListClient(tag, client);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {

				}
			});

		}else{
			clients.get(tag).add(client);
			updateListClient(tag, client);
		}
	}


	@Override
	public void unregisterClient(TypifiedTextClient client) {
		
		for(String tag : clients.keySet()){
			List<TypifiedTextClient> clientList = clients.get(tag);
			if(clientList.contains(client)){
				unregisterClient(tag, client);
			}
		}
		
	}

	@Override
	public void unregisterClient(String tag, TypifiedTextClient client) {
		if(!clients.containsKey(tag)){
			throw new RuntimeException("The texts with the tag " + tag + " are not being managed by this broker.");

		}

		List<TypifiedTextClient> clientList = clients.get(tag);

		if(clients.containsKey(tag)){
			clientList.remove(client);
			if(clientList.isEmpty()){
				dataRefreshRequirements.remove(tag);
				clients.remove(tag);
				dataVersions.remove(tag);
				texts.remove(tag);
			}
		}
	}

	@Override
	public void requireDataRefresh() {
		for(String tag : dataRefreshRequirements.keySet()){
			requireDataRefresh(tag);
		}
	}

	protected boolean requiresDataRefresh(String tag){
		return this.dataRefreshRequirements.get(tag);
	}

	protected void updateListClients(String tag) {
		if(!this.texts.containsKey(tag)){
			throw new RuntimeException("There if no list registered with id \"" + tag + "\"");
		}

		List<TypifiedTextClient> listClients = this.clients.get(tag);

		for(TypifiedTextClient client : listClients){
			this.updateListClient(tag, client);
		}


	}

	protected void updateListClient(String tag, TypifiedTextClient client) {

		List<TypifiedText> texts = this.texts.get(tag);
		int currentDataVersion = this.dataVersions.get(tag).intValue();

		if(!this.clients.get(tag).contains(client))
			throw new RuntimeException("The typified list client is not registered for the list with id : " + tag);

		int clientVersion = client.getTypifiedTextDataVersionNumber();

		if(clientVersion > currentDataVersion || clientVersion < this.NO_DATA_VERSION)
			throw new RuntimeException("Unexpected exception. The client has an inconsistent version number " + clientVersion +
					". Expected between "  + this.NO_DATA_VERSION + " and " + currentDataVersion + ".");

		if(client.getTypifiedTextDataVersionNumber() < currentDataVersion){
			client.setTypifiedTexts(texts);
			client.setTypifiedTextDataVersionNumber(currentDataVersion);			
		}
	}

	protected Integer incrementListDataVersion(String tag) {

		if(!this.texts.containsKey(tag)){
			throw new RuntimeException("There if no list registered with id \"" + tag + "\"");
		}

		Integer currentVersionNumber = this.dataVersions.get(tag);
		Integer dataVersion = new Integer(currentVersionNumber.intValue() + 1);
		setListDataVersion(tag, dataVersion);
		return dataVersion;

	}

	private void setListDataVersion(String tag, Integer dataVersion) {

		if(tag == null || !this.texts.containsKey(tag))
			throw new RuntimeException("There if no list registered with id \"" + tag + "\"");
		if(dataVersion == null || dataVersion < NO_DATA_VERSION){
			throw new RuntimeException("The version is not valid");
		}
		this.dataVersions.put(tag, dataVersion);
	}

	@Override
	public void createText(final String tag, TypifiedText text, final ResponseHandler<TypifiedText> handler) {
		this.service.createText(text, new BigBangAsyncCallback<TypifiedText>() {

			@Override
			public void onSuccess(TypifiedText result) {
				Collection<TypifiedText> textList = texts.get(result.tag);
				textList.add(result);
				incrementDataVersion(result.tag);
				updateListClients(result.tag);
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
		service.deleteText(textId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				for(Collection<TypifiedText> collection: texts.values()){
					for(TypifiedText text : collection){
						collection.remove(text);
						incrementDataVersion(tag);
						for(TypifiedTextClient client : clients.get(tag)){
							client.removeText(text);
							client.setTypifiedTextDataVersionNumber(getCurrentDataVersion());
						}
						handler.onResponse(null);
						return;
					}
				}
				handler.onResponse(null);
			}

			@Override
			public void onFailure(Throwable caught){
				handler.onError(new String[]{"Could not delete the typified text"});
				super.onFailure(caught);
			}
		});
	}

	@Override
	public boolean isClientRegistered(String tag, TypifiedTextClient client) {

		if(clients.containsKey(tag)){
			List<TypifiedTextClient> clientList = clients.get(tag);
			return clientList.contains(client);
		}
		return false;
	}

	@Override
	public void requireDataRefresh(String tag) {
		if(!clients.containsKey(tag)){
			throw new RuntimeException("The texts with tag " + tag + " are not being managed by this broker.");
		}
		dataRefreshRequirements.put(tag, true);
	}

	@Override
	public void notifyItemCreation(String itemId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyItemDeletion(String itemId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyItemUpdate(String itemId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTexts(final String tag, final ResponseHandler<List<TypifiedText>> handler) {
		if(!clients.containsKey(tag)){
			throw new RuntimeException("The texts with tag " + tag + " are not being managed by this broker.");
		}

		if(requiresDataRefresh(tag)){
			service.getTexts(tag, new BigBangAsyncCallback<TypifiedText[]>() {

				@Override
				public void onSuccess(TypifiedText[] result) {
					List<TypifiedText> textList = new ArrayList<TypifiedText>();
					for(int i = 0; i<result.length; i++){
						textList.add(result[i]);
					}
					texts.put(tag, textList);
					incrementDataVersion(tag);
					updateListClients(tag);
					handler.onResponse(textList);
				}

				public void onFailure(Throwable caught){
					handler.onError(new String[]{ new String("Could not get the Typified Texts for the specified tag")});
				}
			});
		}

	}

	@Override
	public void getText(String tag, String textId,
			ResponseHandler<TypifiedText> handler) {
		List<TypifiedText> listTexts = texts.get(tag);

		for(TypifiedText txt : listTexts){
			if(txt.id.equalsIgnoreCase(textId)){
				handler.onResponse(txt);
				return;
			}
		}
		handler.onError(new String[]{"Could not find the text with tag " +tag+ " and id " + textId});
	}

	@Override
	public int incrementDataVersion(String tag) {
		if(!clients.containsKey(tag)){
			throw new RuntimeException("The texts with tag " + tag + " are not being managed by this broker.");
		}

		int newDataVersion = new Integer(dataVersions.get(tag).intValue()+1);
		dataVersions.put(tag, newDataVersion);
		return newDataVersion;


	}

	@Override
	public void updateText(final String tag, final TypifiedText text, final ResponseHandler<TypifiedText> handler) {
		getText(tag, text.id, new ResponseHandler<TypifiedText>() {

			@Override
			public void onResponse(final TypifiedText response) {
				service.saveText(text, new BigBangAsyncCallback<TypifiedText>() {
					@Override
					public void onSuccess(TypifiedText result) {

						List<TypifiedText> textList = texts.get(result.tag);

						for(TypifiedText txt : textList){
							if(txt.id.equalsIgnoreCase(result.id)){
								textList.set(textList.indexOf(txt), result);
								break;
							}
						}
						updateListClients(result.tag);
						handler.onResponse(result);
					}
					@Override
					public void onFailure(Throwable caught){
						handler.onError(new String[]{
								new String("Could not save the text")
						});
						super.onFailure(caught);
					}

				});
			}
			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not get the original document")	
					});
			}

		});

	}

}
