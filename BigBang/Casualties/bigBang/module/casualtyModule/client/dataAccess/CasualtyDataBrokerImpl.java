package bigBang.module.casualtyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.dataAccess.CasualtyDataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.casualtyModule.interfaces.CasualtyService;
import bigBang.module.casualtyModule.interfaces.CasualtyServiceAsync;
import bigBang.module.casualtyModule.shared.CasualtySearchParameter;
import bigBang.module.casualtyModule.shared.CasualtySortParameter;
import bigBang.module.casualtyModule.shared.CasualtySortParameter.SortableField;

public class CasualtyDataBrokerImpl extends DataBroker<Casualty> implements
CasualtyDataBroker {

	protected CasualtyServiceAsync service;
	protected SearchDataBroker<CasualtyStub> searchBroker;

	public CasualtyDataBrokerImpl(){
		this.searchBroker = new CasualtySearchBrokerImpl();
		this.service = CasualtyService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.CASUALTY;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		getCasualty(itemId, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty response) {
				incrementDataVersion();
				for(DataBrokerClient<Casualty> client : clients) {
					((CasualtyDataBrokerClient) client).addCasualty(response);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		incrementDataVersion();
		for(DataBrokerClient<Casualty> client : clients) {
			((CasualtyDataBrokerClient) client).removeCasualty(itemId);
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		getCasualty(itemId, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty response) {
				incrementDataVersion();
				for(DataBrokerClient<Casualty> client : clients) {
					((CasualtyDataBrokerClient) client).updateCasualty(response);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void updateCasualty(Casualty casualty,
			final ResponseHandler<Casualty> handler) {
		service.editCasualty(casualty, new BigBangAsyncCallback<Casualty>() {

			@Override
			public void onResponseSuccess(Casualty result) {
				incrementDataVersion();
				for(DataBrokerClient<Casualty> client : clients) {
					((CasualtyDataBrokerClient) client).updateCasualty(result);
				}
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not update the casualty")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void deleteCasualty(final String casualtyId, String reason, final ResponseHandler<Void> handler) {
		service.deleteCasualty(casualtyId, reason, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				incrementDataVersion();
				handler.onResponse(result);
				for(DataBrokerClient<Casualty> client : clients) {
					((CasualtyDataBrokerClient) client).removeCasualty(casualtyId);
				}
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete the casualty")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getCasualty(String casualtyId, final ResponseHandler<Casualty> handler) {
		service.getCasualty(casualtyId, new BigBangAsyncCallback<Casualty>() {

			@Override
			public void onResponseSuccess(Casualty result) {
				incrementDataVersion();
				for(DataBrokerClient<Casualty> client : clients) {
					((CasualtyDataBrokerClient) client).updateCasualty(result);
				}
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the casualty")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void requireDataRefresh() {
		return;
	}

	@Override
	public SearchDataBroker<CasualtyStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void getCasualtiesForClient(String ownerId,
			final ResponseHandler<Collection<CasualtyStub>> responseHandler) {
		CasualtySearchParameter parameter = new CasualtySearchParameter();
		parameter.ownerId = ownerId;
		
		CasualtySortParameter sort = new CasualtySortParameter(SortableField.DATE, SortOrder.DESC);
		
		getSearchBroker().search(new SearchParameter[]{parameter}, new SortParameter[]{sort}, -1, new ResponseHandler<Search<CasualtyStub>>() {

			@Override
			public void onResponse(Search<CasualtyStub> response) {
				responseHandler.onResponse(response.getResults());
				getSearchBroker().disposeSearch(response.getWorkspaceId());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				responseHandler.onError(new String[]{
						new String("Could not get the casualties for client")
				});
			}
		});
	}

	@Override
	public void close(String casualtyId, final ResponseHandler<Void> handler) {
		service.closeProcess(casualtyId, new BigBangAsyncCallback<Casualty>() {

			@Override
			public void onResponseSuccess(Casualty result) {
				incrementDataVersion();
				handler.onResponse(null);
				for(DataBrokerClient<Casualty> client : clients) {
					((CasualtyDataBrokerClient) client).updateCasualty(result);
				}
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
					new String("Could not close the process")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void reopen(String casualtyId, ResponseHandler<Void> handler) {
//		service.(casualtyId, new BigBangAsyncCallback<Casualty>() {
//
//			@Override
//			public void onResponseSuccess(Casualty result) {
//				incrementDataVersion();
//				for(DataBrokerClient<Casualty> client : clients) {
//					((CasualtyDataBrokerClient) client).updateCasualty(result);
//				}
//				handler.onResponse(null);
//			}
//			
//			@Override
//			public void onResponseFailure(Throwable caught) {
//				handler.onError(new String[]{
//					new String("Could not close the process")	
//				});
//				super.onResponseFailure(caught);
//			}
//		});
	}

}
