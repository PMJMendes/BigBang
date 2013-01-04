package bigBang.module.generalSystemModule.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.CostCenterBroker;
import bigBang.definitions.client.dataAccess.CostCenterDataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.CostCenter;
import bigBang.definitions.shared.User;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.generalSystemModule.interfaces.CostCenterService;
import bigBang.module.generalSystemModule.interfaces.CostCenterServiceAsync;

public class CostCenterBrokerImpl extends DataBroker<CostCenter> implements CostCenterBroker {

	protected CostCenterServiceAsync service;
	protected boolean needsRefresh = true;

	/**
	 * The costructor
	 */
	public CostCenterBrokerImpl(){
		this(CostCenterService.Util.getInstance());
	}

	/**
	 * The constructor
	 * @param service The service to be used by this implementation
	 */
	public CostCenterBrokerImpl(CostCenterServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.COST_CENTER;
		cache.setThreshold(0);
	}

	@Override
	public void getCostCenters(final ResponseHandler<CostCenter[]> handler) {
		if(needsRefresh()){
			this.service.getCostCenterList(new BigBangAsyncCallback<CostCenter[]>() {

				@Override
				public void onResponseSuccess(CostCenter[] result) {
					CostCenterBrokerImpl.this.cache.clear();
					for(int i = 0; i < result.length; i++) {
						cache.add(result[i].id, result[i]);
					}
					incrementDataVersion();
					for(DataBrokerClient<CostCenter> c : getClients()) {
						((CostCenterDataBrokerClient) c).setCostCenters(result);
						((CostCenterDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
					}
					handler.onResponse(result);
					needsRefresh = false;
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the cost centers list")	
					});
					super.onResponseFailure(caught);
				}

			});
		}else{
			int size = cache.getNumberOfEntries();
			CostCenter[] result = new CostCenter[size];

			int i = 0;
			for(Object e : cache.getEntries()){
				result[i] = ((CostCenter) e);
				i++;
			}
			handler.onResponse(result);
		}
	}

	@Override
	public void getCostCenter(String costCenterId,
			ResponseHandler<CostCenter> handler) {
		if(!cache.contains(costCenterId)){
			handler.onError(new String[]{
					new String("Could not get the requested cost center. id: " + costCenterId)
			});
		}else{
			handler.onResponse((CostCenter) cache.get(costCenterId));
		}
	}


	@Override
	public void addCostCenter(CostCenter costCenter,
			final ResponseHandler<CostCenter> handler) {
		this.service.createCostCenter(costCenter, new BigBangAsyncCallback<CostCenter>() {

			@Override
			public void onResponseSuccess(CostCenter result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<CostCenter> c : getClients()) {
					((CostCenterDataBrokerClient) c).addCostCenter(result);
					((CostCenterDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.GeneralSystemProcess.MANAGE_COST_CENTERS, result.id));
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create cost center")
				});
				super.onResponseFailure(caught);
			}
			
		});
	}

	@Override
	public void updateCostCenter(CostCenter costCenter,
			final ResponseHandler<CostCenter> handler) {
		this.service.saveCostCenter(costCenter, new BigBangAsyncCallback<CostCenter>() {

			@Override
			public void onResponseSuccess(CostCenter result) {
				cache.update(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<CostCenter> c : getClients()) {
					((CostCenterDataBrokerClient) c).updateCostCenter(result);
					((CostCenterDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.GeneralSystemProcess.MANAGE_COST_CENTERS, result.id));
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not update cost center")
				});
				super.onResponseFailure(caught);
			}
			
		});
	}

	@Override
	public void removeCostCenter(final String costCenterId,
			final ResponseHandler<CostCenter> handler) {
		service.deleteCostCenter(costCenterId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				cache.remove(costCenterId);
				incrementDataVersion();
				for(DataBrokerClient<CostCenter> c : getClients()) {
					((CostCenterDataBrokerClient) c).removeCostCenter(costCenterId);
					((CostCenterDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.GeneralSystemProcess.MANAGE_COST_CENTERS, costCenterId));

				handler.onResponse(null);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete cost center.")
				});
				super.onResponseFailure(caught);
			}
			
		});
	}

	@Override
	public void requireDataRefresh() {
		this.needsRefresh = true;
	}

	/**
	 * Returns whether or not the broker needs to refresh the data
	 * @return
	 */
	protected boolean needsRefresh(){
		return this.needsRefresh;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		requireDataRefresh();
		this.getCostCenter(itemId, new ResponseHandler<CostCenter>() {

			@Override
			public void onResponse(CostCenter response) {
				cache.add(response.id, response);
				incrementDataVersion();

				for(DataBrokerClient<CostCenter> client : clients){
					((CostCenterDataBrokerClient) client).addCostCenter(response);
					((CostCenterDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
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
		requireDataRefresh();
		cache.remove(itemId);
		incrementDataVersion();
		for(DataBrokerClient<CostCenter> client : clients){
			((CostCenterDataBrokerClient) client).removeCostCenter(itemId);
			((CostCenterDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		requireDataRefresh();
		getCostCenter(itemId, new ResponseHandler<CostCenter>() {
			
			@Override
			public void onResponse(CostCenter response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<CostCenter> client : clients){
					((CostCenterDataBrokerClient) client).updateCostCenter(response);
					((CostCenterDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getCostCenterMembers(String ownerId,
			final ResponseHandler<Collection<User>> responseHandler) {

		service.getCostCenterUsers(ownerId, new BigBangAsyncCallback<User[]>() {

			@Override
			public void onResponseSuccess(User[] result) {
				ArrayList<User> users = new ArrayList<User>();
				for(User user : result) {
					users.add(user);
				}
				responseHandler.onResponse(users);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
					new String("Could not get the cost center members")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

}
