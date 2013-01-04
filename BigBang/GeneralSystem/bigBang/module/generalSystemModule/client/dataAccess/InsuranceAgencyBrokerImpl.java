package bigBang.module.generalSystemModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuranceAgencyBroker;
import bigBang.definitions.client.dataAccess.InsuranceAgencyDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
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
				public void onResponseSuccess(InsuranceAgency[] result) {
					InsuranceAgencyBrokerImpl.this.cache.clear();
					for(int i = 0; i < result.length; i++) {
						cache.add(result[i].id, result[i]);
					}
					incrementDataVersion();
					for(DataBrokerClient<InsuranceAgency> c : getClients()) {
						((InsuranceAgencyDataBrokerClient) c).setInsuranceAgencies(result);
						((InsuranceAgencyDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
					}
					handler.onResponse(result);
					needsRefresh = false;
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get insurance agencies list")	
					});
					super.onResponseFailure(caught);
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
		if(!cache.contains(insuranceAgencyId)){
			handler.onError(new String[]{
					new String("Could not find the insurance agency")
			});
		}else{
			handler.onResponse((InsuranceAgency) cache.get(insuranceAgencyId));
		}
	}


	@Override
	public void addInsuranceAgency(InsuranceAgency agency,
			final ResponseHandler<InsuranceAgency> handler) {
		this.service.createInsuranceAgency(agency, new BigBangAsyncCallback<InsuranceAgency>() {

			@Override
			public void onResponseSuccess(InsuranceAgency result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<InsuranceAgency> c : getClients()) {
					((InsuranceAgencyDataBrokerClient) c).addInsuranceAgency(result);
					((InsuranceAgencyDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.GeneralSystemProcess.MANAGE_COMPANIES, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create insurance agency")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void updateInsuranceAgency(InsuranceAgency insuranceAgency,
			final ResponseHandler<InsuranceAgency> handler) {
		this.service.saveInsuranceAgency(insuranceAgency, new BigBangAsyncCallback<InsuranceAgency>() {

			@Override
			public void onResponseSuccess(InsuranceAgency result) {
				cache.update(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<InsuranceAgency> c : getClients()) {
					((InsuranceAgencyDataBrokerClient) c).updateInsuranceAgency(result);
					((InsuranceAgencyDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.GeneralSystemProcess.MANAGE_COMPANIES, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not update insurance agency")	
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void removeInsuranceAgency(final String insuranceAgencyId,
			final ResponseHandler<InsuranceAgency> handler) {
		service.deleteInsuranceAgency(insuranceAgencyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				cache.remove(insuranceAgencyId);
				incrementDataVersion();
				for(DataBrokerClient<InsuranceAgency> c : getClients()) {
					((InsuranceAgencyDataBrokerClient) c).removeInsuranceAgency(insuranceAgencyId);
					((InsuranceAgencyDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.GeneralSystemProcess.MANAGE_COMPANIES, insuranceAgencyId));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete insurance agency")
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
	 * Returns whether or not the broker needs to refresh its data
	 * @return true if needs to refresh the data, false otherwise
	 */
	public boolean needsRefresh(){
		return this.needsRefresh;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		requireDataRefresh();
		getInsuranceAgency(itemId, new ResponseHandler<InsuranceAgency>() {

			@Override
			public void onResponse(InsuranceAgency response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<InsuranceAgency> c : getClients()) {
					((InsuranceAgencyDataBrokerClient) c).addInsuranceAgency(response);
					((InsuranceAgencyDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
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
		for(DataBrokerClient<InsuranceAgency> c : getClients()) {
			((InsuranceAgencyDataBrokerClient) c).removeInsuranceAgency(itemId);
			((InsuranceAgencyDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		requireDataRefresh();
		getInsuranceAgency(itemId, new ResponseHandler<InsuranceAgency>() {

			@Override
			public void onResponse(InsuranceAgency response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<InsuranceAgency> c : getClients()) {
					((InsuranceAgencyDataBrokerClient) c).updateInsuranceAgency(response);
					((InsuranceAgencyDataBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

}
