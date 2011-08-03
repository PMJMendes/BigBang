package bigBang.module.clientModule.client.dataAccess;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.broker.ClientProcessBroker;
import bigBang.definitions.client.brokerClient.ClientProcessDataBrokerClient;
import bigBang.definitions.client.types.Client;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.dataAccess.DataBroker;
import bigBang.library.client.dataAccess.DataBrokerClient;
import bigBang.library.client.response.ResponseHandler;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.interfaces.ClientServiceAsync;

public class ClientProcessBrokerImpl extends DataBroker<Client> implements ClientProcessBroker {

	protected ClientServiceAsync service;
	protected boolean requiresRefresh = true;
	
	public ClientProcessBrokerImpl(){
		this(ClientService.Util.getInstance());
	}

	public ClientProcessBrokerImpl(ClientServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.CLIENT;
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
					for(DataBrokerClient<Client> bc : clients){
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
				for(DataBrokerClient<Client> bc : clients){
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
				for(DataBrokerClient<Client> bc : clients){
					((ClientProcessDataBrokerClient) bc).updateClient(result);
					((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void removeClient(final String clientId, final ResponseHandler<String> handler) {
		this.service.deleteClient(clientId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				cache.remove(clientId);
				incrementDataVersion();
				for(DataBrokerClient<Client> bc : clients){
					((ClientProcessDataBrokerClient) bc).removeClient(clientId);
					((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
				}
				handler.onResponse(clientId);
			}
		});
	}

	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

}
