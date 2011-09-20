package bigBang.module.generalSystemModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuranceAgencyBroker;
import bigBang.definitions.client.dataAccess.InsuranceAgencyDataBrokerClient;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.generalSystemModule.interfaces.InsuranceAgencyService;
import bigBang.module.generalSystemModule.interfaces.InsuranceAgencyServiceAsync;

public class InsuranceAgencyBrokerImpl extends DataBroker<InsuranceAgency> implements InsuranceAgencyBroker {

	protected InsuranceAgencyServiceAsync service;
	protected boolean needsRefresh = true;

	/**
	 * The constructor
	 */
	public InsuranceAgencyBrokerImpl(){
		this(InsuranceAgencyService.Util.getInstance());
	}

	/**
	 * The constructor
	 * @param service The service to be used by this implementation
	 */
	public InsuranceAgencyBrokerImpl(InsuranceAgencyServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.INSURANCE_AGENCY;
		cache.setThreshold(0);
	}

	@Override
	public void getInsuranceAgencies(final ResponseHandler<InsuranceAgency[]> handler) {
		if(needsRefresh()) {
			this.service.getInsuranceAgencies(new BigBangAsyncCallback<InsuranceAgency[]>() {

				@Override
				public void onSuccess(InsuranceAgency[] result) {
					InsuranceAgencyBrokerImpl.this.cache.clear();
					for(int i = 0; i < result.length; i++) {
						cache.add(result[i].id, result[i]);
					}
					for(DataBrokerClient<InsuranceAgency> c : getClients()) {
						((InsuranceAgencyDataBrokerClient) c).setInsuranceAgencies(result);
					}
					handler.onResponse(result);
					needsRefresh = false;
				}
			});
		}else{
			int size = this.cache.getNumberOfEntries();
			InsuranceAgency[] agencies = new InsuranceAgency[size];
			
			int i = 0;
			for(Object o : this.cache.getEntries()){
				agencies[i] = ((InsuranceAgency) o);
				i++;
			}
			
			handler.onResponse(agencies);
		}
	}

	@Override
	public void getInsuranceAgency(String insuranceAgencyId,
			ResponseHandler<InsuranceAgency> handler) {
		if(!cache.contains(insuranceAgencyId))
			throw new RuntimeException("The requested insurance agency could not be fould locally. id:\""+insuranceAgencyId+"\"");
		handler.onResponse((InsuranceAgency) cache.get(insuranceAgencyId));
	}


	@Override
	public void addInsuranceAgency(InsuranceAgency agency,
			final ResponseHandler<InsuranceAgency> handler) {
		this.service.createInsuranceAgency(agency, new BigBangAsyncCallback<InsuranceAgency>() {

			@Override
			public void onSuccess(InsuranceAgency result) {
				cache.add(result.id, result);
				for(DataBrokerClient<InsuranceAgency> c : getClients()) {
					((InsuranceAgencyDataBrokerClient) c).addInsuranceAgency(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void updateInsuranceAgency(InsuranceAgency insuranceAgency,
			final ResponseHandler<InsuranceAgency> handler) {
		this.service.saveInsuranceAgency(insuranceAgency, new BigBangAsyncCallback<InsuranceAgency>() {

			@Override
			public void onSuccess(InsuranceAgency result) {
				cache.update(result.id, result);
				for(DataBrokerClient<InsuranceAgency> c : getClients()) {
					((InsuranceAgencyDataBrokerClient) c).updateInsuranceAgency(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void removeInsuranceAgency(final String insuranceAgencyId,
			final ResponseHandler<InsuranceAgency> handler) {
		service.deleteInsuranceAgency(insuranceAgencyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				cache.remove(insuranceAgencyId);
				for(DataBrokerClient<InsuranceAgency> c : getClients()) {
					((InsuranceAgencyDataBrokerClient) c).removeInsuranceAgency(insuranceAgencyId);
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
	 * Returns whether or not the broker needs to refresh its data
	 * @return true if needs to refresh the data, false otherwise
	 */
	public boolean needsRefresh(){
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
