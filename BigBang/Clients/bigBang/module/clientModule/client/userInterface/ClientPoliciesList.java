package bigBang.module.clientModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;

import com.google.gwt.event.logical.shared.AttachEvent;

public class ClientPoliciesList extends FilterableList<InsurancePolicyStub> implements InsurancePolicyDataBrokerClient{

	public static class Entry extends InsurancePolicySearchPanel.Entry {
		public Entry(InsurancePolicyStub policy) {
			super(policy);
		}

		public <I extends Object> void setInfo(I info) {
			super.setInfo(info);
			if(info != null){
				setLeftWidget(this.statusIcon);
			}
		};
	}

	protected InsurancePolicyBroker broker;
	protected String ownerId;
	protected int dataVersion;

	public ClientPoliciesList(){
		this.showFilterField(false);
		this.showSearchField(true);

		broker = (InsurancePolicyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);

		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					setOwner(ownerId);
				}else{
					discardOwner();
				}
			}
		});
	}

	public void setOwner(String ownerId){
		discardOwner();
		if(ownerId != null){
			this.broker.registerClient(this);
			broker.getClientPolicies(ownerId, new ResponseHandler<Collection<InsurancePolicyStub>>() {

				@Override
				public void onResponse(Collection<InsurancePolicyStub> response) {
					ClientPoliciesList.this.clear();
					for(InsurancePolicyStub s : response){
						addEntry(s);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {}
			});
		}
		this.ownerId = ownerId;
	}

	public void discardOwner(){
		this.clear();
		if(ownerId != null) {
			broker.unregisterClient(this);
			this.ownerId = null;
		}
	}

	public void addEntry(InsurancePolicyStub policy) {
		add(new Entry(policy));
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
			dataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
			return dataVersion;
		}
		return -1;
	}

	@Override
	public void addInsurancePolicy(InsurancePolicy policy) {
		if(this.ownerId != null){
			if(policy.clientId.equalsIgnoreCase(this.ownerId)){
				this.addEntry(policy);
			}
		}
	}

	@Override
	public void updateInsurancePolicy(InsurancePolicy policy) {
		for(ValueSelectable<InsurancePolicyStub> s : this){
			if(s.getValue().id.equalsIgnoreCase(policy.id)){
				s.setValue(policy);
				break;
			}
		}
	}

	@Override
	public void removeInsurancePolicy(String policyId) {
		for(ValueSelectable<InsurancePolicyStub> s : this) {
			if(s.getValue().id.equalsIgnoreCase(policyId)){
				remove(s);
				break;
			}
		}
	}

	@Override
	public void remapItemId(String oldId, String newId) {
		return;
	}
}