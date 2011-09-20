package bigBang.module.generalSystemModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.CostCenterBroker;
import bigBang.definitions.client.dataAccess.CostCenterDataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.CostCenter;
import bigBang.library.client.BigBangAsyncCallback;
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
				public void onSuccess(CostCenter[] result) {
					CostCenterBrokerImpl.this.cache.clear();
					for(int i = 0; i < result.length; i++) {
						cache.add(result[i].id, result[i]);
					}
					for(DataBrokerClient<CostCenter> c : getClients()) {
						((CostCenterDataBrokerClient) c).setCostCenters(result);
					}
					handler.onResponse(result);
					needsRefresh = false;
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
		if(!cache.contains(costCenterId))
			throw new RuntimeException("The requested cost center could not be fould locally. id:\""+costCenterId+"\"");
		handler.onResponse((CostCenter) cache.get(costCenterId));
	}


	@Override
	public void addCostCenter(CostCenter costCenter,
			final ResponseHandler<CostCenter> handler) {
		this.service.createCostCenter(costCenter, new BigBangAsyncCallback<CostCenter>() {

			@Override
			public void onSuccess(CostCenter result) {
				cache.add(result.id, result);
				for(DataBrokerClient<CostCenter> c : getClients()) {
					((CostCenterDataBrokerClient) c).addCostCenter(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void updateCostCenter(CostCenter costCenter,
			final ResponseHandler<CostCenter> handler) {
		this.service.saveCostCenter(costCenter, new BigBangAsyncCallback<CostCenter>() {

			@Override
			public void onSuccess(CostCenter result) {
				cache.update(result.id, result);
				for(DataBrokerClient<CostCenter> c : getClients()) {
					((CostCenterDataBrokerClient) c).updateCostCenter(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void removeCostCenter(final String costCenterId,
			final ResponseHandler<CostCenter> handler) {
		service.deleteCostCenter(costCenterId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				cache.remove(costCenterId);
				for(DataBrokerClient<CostCenter> c : getClients()) {
					((CostCenterDataBrokerClient) c).removeCostCenter(costCenterId);
				}
				handler.onResponse(null);
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
		//TODO FJVC
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		requireDataRefresh();
		//TODO
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		requireDataRefresh();
		//TODO
	}

}
