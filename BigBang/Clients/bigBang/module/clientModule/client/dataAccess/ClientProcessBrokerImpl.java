package bigBang.module.clientModule.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.interfaces.BigBangProcessService;
import bigBang.library.interfaces.BigBangProcessServiceAsync;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.interfaces.ClientServiceAsync;

public class ClientProcessBrokerImpl extends DataBroker<Client> implements ClientProcessBroker {

	protected ClientServiceAsync service;
	protected BigBangProcessServiceAsync subProcessesService;
	protected SearchDataBroker<ClientStub> searchBroker;
	protected boolean requiresRefresh = true;

	public ClientProcessBrokerImpl(){
		this(ClientService.Util.getInstance());
	}

	public ClientProcessBrokerImpl(ClientServiceAsync service) {
		this.service = service;
		this.subProcessesService = BigBangProcessService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.CLIENT;
		this.searchBroker = new ClientSearchDataBroker(this.service);
	}

	@Override
	public void getClient(final String clientId, final ResponseHandler<Client> handler) {
		if(cache.contains(clientId) && !requiresRefresh){
			handler.onResponse((Client) cache.get(clientId));
		}else{
			this.service.getClient(clientId, new BigBangAsyncCallback<Client>() {

				@Override
				public void onResponseSuccess(Client result) {
					cache.add(clientId, result);
					incrementDataVersion();
					for(DataBrokerClient<Client> bc : getClients()){
						((ClientProcessDataBrokerClient) bc).updateClient(result);
						((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
					}
					handler.onResponse(result);
					requiresRefresh = false;
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested client")
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public void addClient(Client client, final ResponseHandler<Client> handler) {
		this.service.createClient(client, new BigBangAsyncCallback<Client>() {

			@Override
			public void onResponseSuccess(Client result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Client> bc : getClients()){
					((ClientProcessDataBrokerClient) bc).addClient(result);
					((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.GeneralSystemProcess.CREATE_CLIENT, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create the client")
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void updateClient(Client client, final ResponseHandler<Client> handler) {
		this.service.editClient(client, new BigBangAsyncCallback<Client>() {

			@Override
			public void onResponseSuccess(Client result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Client> bc : getClients()){
					((ClientProcessDataBrokerClient) bc).updateClient(result);
					((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.UPDATE_CLIENT, result.id));

				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not save the client")
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void removeClient(final String clientId, String reasonId, final ResponseHandler<String> handler) {
		ClientProcessBrokerImpl.this.service.deleteClient(clientId, reasonId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				cache.remove(clientId);
				incrementDataVersion();
				for(DataBrokerClient<Client> bc : getClients()){
					((ClientProcessDataBrokerClient) bc).removeClient(clientId);
					((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.DELETE_CLIENT, clientId));

				handler.onResponse(clientId);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete the client")
				});
				super.onResponseFailure(caught);
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

	@Override
	public void createRiskAnalisys(RiskAnalysis riskAnalisys,
			final ResponseHandler<RiskAnalysis> handler) {
		service.createRiskAnalisys(riskAnalisys, new BigBangAsyncCallback<RiskAnalysis>() {

			@Override
			public void onResponseSuccess(RiskAnalysis result) {
				//TODO
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.CREATE_RISK_ANALISYS, result.clientId));				

				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create new Risk analisys")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createPolicy(InsurancePolicy policy,
			final ResponseHandler<InsurancePolicy> handler) {
		service.createPolicy(policy, new BigBangAsyncCallback<InsurancePolicy>() {

			@Override
			public void onResponseSuccess(InsurancePolicy result) {
				//TODO
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.CREATE_POLICY, result.clientId));

				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create new Policy")	
				});
				super.onResponseFailure(caught);			
			}
		});
	}

	@Override
	public void createCasualty(Casualty casualty,
			final ResponseHandler<Casualty> handler) {
		service.createCasualty(casualty, new BigBangAsyncCallback<Casualty>() {

			@Override
			public void onResponseSuccess(Casualty result) {
				//TODO
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.CREATE_CASUALTY, result.clientId));

				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create new Casualty")	
				});
				super.onResponseFailure(caught);			
			}
		});
	}

	@Override
	public void mergeWithClient(String originalId, String receptorId,
			final ResponseHandler<Client> handler) {
		service.mergeWithClient(originalId, receptorId, new BigBangAsyncCallback<Client>() {

			@Override
			public void onResponseSuccess(Client result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Client> bc : getClients()){
					((ClientProcessDataBrokerClient) bc).updateClient(result);
					((ClientProcessDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.CLIENT, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.MERGE_CLIENT, result.id));

				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not merge the clients")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createManagerTransfer(String[] dataObjectIds, String managerId,
			final ResponseHandler<ManagerTransfer> handler) {
		ManagerTransfer transfer = new ManagerTransfer();
		transfer.newManagerId = managerId;
		transfer.dataObjectIds = dataObjectIds;

		if(dataObjectIds.length > 1){
			service.massCreateManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

				@Override
				public void onResponseSuccess(ManagerTransfer result) {
					for(int i = 0; i < result.dataObjectIds.length; i++) {
						requireDataRefresh();
						getClient(result.dataObjectIds[i], new ResponseHandler<Client>(){

							@Override
							public void onResponse(Client response) {
								for(DataBrokerClient<Client> c : ClientProcessBrokerImpl.this.clients) {
									ClientProcessDataBrokerClient b = (ClientProcessDataBrokerClient)c;
									b.updateClient(response);
								}
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								return;
							}
						});
					}
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.CREATE_MANAGER_TRANSFER, result.newManagerId));
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not create the manager transfer")	
					});
					super.onResponseFailure(caught);
				}

			});
		}else{
			service.createManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

				@Override
				public void onResponseSuccess(ManagerTransfer result) {
					for(int i = 0; i < result.dataObjectIds.length; i++) {
						getClient(result.dataObjectIds[i], new ResponseHandler<Client>(){

							@Override
							public void onResponse(Client response) {
								for(DataBrokerClient<Client> c : ClientProcessBrokerImpl.this.clients) {
									ClientProcessDataBrokerClient b = (ClientProcessDataBrokerClient)c;
									b.updateClient(response);
								}
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								return;
							}
						});
					}
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.CREATE_MANAGER_TRANSFER, result.newManagerId));

					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not create manager transfer")	
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public void getClientSubProcesses(String clientId, final ResponseHandler<Collection<BigBangProcess>> handler){
		this.subProcessesService.getSubProcesses(clientId, new BigBangAsyncCallback<BigBangProcess[]>() {

			@Override
			public void onResponseSuccess(BigBangProcess[] result) {
				List<BigBangProcess> processesList = new ArrayList<BigBangProcess>();
				for(int i = 0; i < result.length; i++) {
					processesList.add(result[i]);
				}
				handler.onResponse(processesList);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the client subprocesses")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getClientSubProcess(String clientId, final String subProcessId,
			final ResponseHandler<BigBangProcess> handler) {
		this.subProcessesService.getSubProcesses(clientId, new BigBangAsyncCallback<BigBangProcess[]>() {

			@Override
			public void onResponseSuccess(BigBangProcess[] result) {
				for(int i = 0; i < result.length; i++){
					if(result[i].dataId.equalsIgnoreCase(subProcessId)){
						handler.onResponse(result[i]);
						return;
					}
				}
				handler.onError(new String[]{
						new String("Could not get the client subprocess")
				});
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the client subprocess")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void notifyItemCreation(String itemId) {
		requireDataRefresh();
		this.getClient(itemId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		if(this.cache.contains(itemId)){
			cache.remove(itemId);
		}
		for(DataBrokerClient<Client> c : this.clients) {
			ClientProcessDataBrokerClient b = (ClientProcessDataBrokerClient)c;
			b.removeClient(itemId);
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		requireDataRefresh();
		this.getClient(itemId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void sendMessage(Conversation conversation,
			final ResponseHandler<Conversation> handler) {
		service.sendMessage(conversation, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.CONVERSATION, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not send the message")		
				});	
				super.onResponseFailure(caught);
			}


		});

	}

	@Override
	public void receiveMessage(Conversation conversation,
			final ResponseHandler<Conversation> handler) {
		service.receiveMessage(conversation, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.CONVERSATION, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not send the message")		
				});	
				super.onResponseFailure(caught);
			}


		});		
	}

}
