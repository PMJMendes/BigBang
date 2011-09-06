package bigBang.module.clientModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.interfaces.ClientServiceAsync;

public class ClientProcessBrokerImpl extends DataBroker<Client> implements ClientProcessBroker {

	protected ClientServiceAsync service;
	protected SearchDataBroker<ClientStub> searchBroker;
	protected boolean requiresRefresh = true;
	
	public ClientProcessBrokerImpl(){
		this(ClientService.Util.getInstance());
	}

	public ClientProcessBrokerImpl(ClientServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.CLIENT;
		this.searchBroker = new ClientSearchDataBroker(this.service);
	}

	@Override
	public void getClient(final String clientId, final ResponseHandler<Client> handler) {
		if(cache.contains(clientId) && !requiresRefresh){ //TODO IMPORTANT WHEN TO UPDATE FJVC
			handler.onResponse((Client) cache.get(clientId));
		}else{
			this.service.getClient(clientId, new BigBangAsyncCallback<Client>() {

				@Override
				public void onSuccess(Client result) {
					cache.add(clientId, result);
					incrementDataVersion();
					for(DataBrokerClient<Client> bc : getClients()){
						((ClientProcessDataBrokerClient) bc).addClient(result);
						((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
					}
					handler.onResponse(result);
					requiresRefresh = false;
				}
			});
		}
	}
	

	@Override
	public void addClient(Client client, final ResponseHandler<Client> handler) {
		this.service.createClient(client, new BigBangAsyncCallback<Client>() {

			@Override
			public void onSuccess(Client result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Client> bc : getClients()){
					((ClientProcessDataBrokerClient) bc).addClient(result);
					((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void updateClient(Client client, final ResponseHandler<Client> handler) {
		this.service.editClient(client, new BigBangAsyncCallback<Client>() {

			@Override
			public void onSuccess(Client result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Client> bc : getClients()){
					((ClientProcessDataBrokerClient) bc).updateClient(result);
					((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void removeClient(final String clientId, final ResponseHandler<String> handler) {
		this.getClient(clientId, new ResponseHandler<Client>() {
			
			@Override
			public void onResponse(Client response) {
				ClientProcessBrokerImpl.this.service.deleteClient(clientId, response.processId, new BigBangAsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						cache.remove(clientId);
						incrementDataVersion();
						for(DataBrokerClient<Client> bc : getClients()){
							((ClientProcessDataBrokerClient) bc).removeClient(clientId);
							((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
						}
						handler.onResponse(clientId);
					}
				});
				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

	@Override
	public SearchDataBroker<ClientStub> getSearchBroker() {
		return this.searchBroker;
	}

}
