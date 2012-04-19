package bigBang.module.casualtyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.casualtyModule.interfaces.SubCasualtyService;
import bigBang.module.casualtyModule.interfaces.SubCasualtyServiceAsync;
import bigBang.module.casualtyModule.shared.SubCasualtySearchParameter;
import bigBang.module.casualtyModule.shared.SubCasualtySortParameter;
import bigBang.module.casualtyModule.shared.SubCasualtySortParameter.SortableField;

public class SubCasualtyDataBrokerImpl extends DataBroker<SubCasualty>
implements SubCasualtyDataBroker{

	protected SubCasualtyServiceAsync service;
	protected SubCasualtySearchDataBroker searchBroker;

	public SubCasualtyDataBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.SUB_CASUALTY;
		this.service = SubCasualtyService.Util.getInstance();
		this.searchBroker = new SubCasualtySearchDataBroker();
	}

	@Override
	public void requireDataRefresh() {
		return;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		getSubCasualty(itemId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				incrementDataVersion();
				for(DataBrokerClient<SubCasualty> client : getClients()) {
					((SubCasualtyDataBrokerClient) client).addSubCasualty(response);
					((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
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
		for(DataBrokerClient<SubCasualty> client : getClients()) {
			((SubCasualtyDataBrokerClient) client).removeSubCasualty(itemId);
			((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		getSubCasualty(itemId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				incrementDataVersion();
				for(DataBrokerClient<SubCasualty> client : getClients()) {
					((SubCasualtyDataBrokerClient) client).updateSubCasualty(response);
					((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getSubCasualty(String id, final ResponseHandler<SubCasualty> handler) {
		service.getSubCasualty(id, new BigBangAsyncCallback<SubCasualty>() {

			@Override
			public void onResponseSuccess(SubCasualty result) {
				incrementDataVersion();
				for(DataBrokerClient<SubCasualty> client : getClients()) {
					((SubCasualtyDataBrokerClient) client).updateSubCasualty(result);
					((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the sub casualty")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void updateSubCasualty(SubCasualty subCasualty,
			final ResponseHandler<SubCasualty> handler) {
		service.editSubCasualty(subCasualty, new BigBangAsyncCallback<SubCasualty>() {

			@Override
			public void onResponseSuccess(SubCasualty result) {
				incrementDataVersion();
				for(DataBrokerClient<SubCasualty> client : getClients()) {
					((SubCasualtyDataBrokerClient) client).updateSubCasualty(result);
					((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not update the sub casualty")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void deleteSubCasualty(final String subCasualtyId, String reason,
			final ResponseHandler<Void> handler) {
		service.deleteSubCasualty(subCasualtyId, reason, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				incrementDataVersion();
				for(DataBrokerClient<SubCasualty> client : getClients()) {
					((SubCasualtyDataBrokerClient) client).removeSubCasualty(subCasualtyId);
					((SubCasualtyDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete the sub casualty")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getSubCasualties(String ownerId,
			final ResponseHandler<Collection<SubCasualtyStub>> responseHandler) {

		SubCasualtySearchParameter parameter = new SubCasualtySearchParameter();
		parameter.ownerId = ownerId;

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		SubCasualtySortParameter sort = new SubCasualtySortParameter(SortableField.NUMBER, SortOrder.DESC);

		SortParameter[] sorts = new SortParameter[]{
				sort
		};

		getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<SubCasualtyStub>>() {

			@Override
			public void onResponse(Search<SubCasualtyStub> response) {
				responseHandler.onResponse(response.getResults());
				getSearchBroker().disposeSearch(response.getWorkspaceId());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				responseHandler.onError(new String[]{
					new String("Could not get the sub casualties for the given owner")	
				});
			}
		});
	}

	@Override
	public SearchDataBroker<SubCasualtyStub> getSearchBroker() {
		return this.searchBroker;
	}
}
