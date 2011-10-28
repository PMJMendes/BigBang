package bigBang.module.clientModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest.Cancellation;
import bigBang.definitions.shared.InfoOrDocumentRequest.Response;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.interfaces.InfoOrDocumentRequestServiceAsync;
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
		if(cache.contains(clientId) && !requiresRefresh){
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

	@Override
	public void createRiskAnalisys(String clientId, RiskAnalysis riskAnalisys,
			final ResponseHandler<RiskAnalysis> handler) {
		service.createRiskAnalisys(clientId, riskAnalisys, new BigBangAsyncCallback<RiskAnalysis>() {

			@Override
			public void onSuccess(RiskAnalysis result) {
				//TODO
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void createInsurancePolicy(String clientId, InsurancePolicy policy,
			final ResponseHandler<InsurancePolicy> handler) {
		service.createPolicy(clientId, policy, new BigBangAsyncCallback<InsurancePolicy>() {

			@Override
			public void onSuccess(InsurancePolicy result) {
				((InsurancePolicyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY)).notifyItemCreation(result.id);
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void createQuoteRequest(String clientId, QuoteRequest quoteRequest,
			final ResponseHandler<QuoteRequest> handler) {
		service.createQuoteRequest(clientId, quoteRequest, new BigBangAsyncCallback<QuoteRequest>() {

			@Override
			public void onSuccess(QuoteRequest result) {
				//TODO
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void createCasualty(String clientId, Casualty casualty,
			final ResponseHandler<Casualty> handler) {
		service.createCasualty(clientId, casualty, new BigBangAsyncCallback<Casualty>() {

			@Override
			public void onSuccess(Casualty result) {
				//TODO
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void mergeWithClient(String originalId, String receptorId,
			final ResponseHandler<Client> handler) {
		service.mergeWithClient(originalId, receptorId, new BigBangAsyncCallback<Client>() {
			
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
	public void createInfoOrDocumentRequest(
			InfoOrDocumentRequest request,
			final ResponseHandler<InfoOrDocumentRequest> handler) {
		service.createInfoOrDocumentRequest(request, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onSuccess(InfoOrDocumentRequest result) {
				//TODO
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void repeatRequest(InfoOrDocumentRequest request,
			final ResponseHandler<InfoOrDocumentRequest> handler) {
		InfoOrDocumentRequestServiceAsync infoService = InfoOrDocumentRequestService.Util.getInstance();
		infoService.repeatRequest(request, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onSuccess(InfoOrDocumentRequest result) {
				//TODO
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void receiveInfoOrDocumentRequestResponse(Response response,
			final ResponseHandler<InfoOrDocumentRequest> handler) {
		InfoOrDocumentRequestServiceAsync infoService = InfoOrDocumentRequestService.Util.getInstance();
		infoService.receiveResponse(response, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onSuccess(InfoOrDocumentRequest result) {
				// TODO Auto-generated method stub
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void cancelInfoOrDocumentRequest(Cancellation cancellation,
			final ResponseHandler<Void> handler) {
		InfoOrDocumentRequestServiceAsync infoService = InfoOrDocumentRequestService.Util.getInstance();
		infoService.cancelRequest(cancellation, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void createManagerTransfer(String[] clientIds, String managerId,
			final ResponseHandler<ManagerTransfer> handler) {
		ManagerTransfer transfer = new ManagerTransfer();
		transfer.newManagerId = managerId;
		transfer.managedProcessIds = clientIds;
			
		if(clientIds.length > 1){
			service.massCreateManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

				@Override
				public void onSuccess(ManagerTransfer result) {
					handler.onResponse(result);
				}
			});
		}else{
			service.createManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

				@Override
				public void onSuccess(ManagerTransfer result) {
					handler.onResponse(result);
				}
			});
		}
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

}
