package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;

public class SubPolicyInsuredObjectsList extends FilterableList<InsuredObjectStub> {

	protected class Entry extends ListEntry<InsuredObjectStub> {

		public Entry(InsuredObjectStub value) {
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			InsuredObjectStub o = (InsuredObjectStub) info;
			setTitle(o.unitIdentification);
		};
	}

	protected InsuranceSubPolicyBroker subPolicyBroker;
	protected InsuredObjectDataBrokerClient insuredObjectsBrokerClient;
	protected InsuredObjectDataBroker insuredObjectsBroker;

	protected String ownerId;

	public SubPolicyInsuredObjectsList(){
		this.insuredObjectsBrokerClient = getInsuredObjectsBrokerClient();
		this.subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		this.insuredObjectsBroker = (InsuredObjectDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS);
		this.insuredObjectsBroker.registerClient(this.insuredObjectsBrokerClient);
		showFilterField(false);
	}

	public void setOwner(String ownerId){
		this.ownerId = ownerId;
		if(ownerId == null) {
			clear();
		}else //if(!subPolicyBroker.isTemp(ownerId)){
			this.insuredObjectsBroker.getProcessInsuredObjects(ownerId, new ResponseHandler<Collection<InsuredObjectStub>>() {

				@Override
				public void onResponse(Collection<InsuredObjectStub> response) {
					clear();
					for(InsuredObjectStub o : response){
						addEntry(o);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		//}
	}

	protected void addEntry(InsuredObjectStub object){
		this.add(new Entry(object));
	}

	protected InsuredObjectDataBrokerClient getInsuredObjectsBrokerClient(){
		return new InsuredObjectDataBrokerClient() {

			protected int version;

			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS)){
					version = number;
				}
			}

			@Override
			public int getDataVersion(String dataElementId) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS)){
					return version;
				}
				return -1;
			}

			@Override
			public void updateInsuredObject(InsuredObject object) {
				for(ValueSelectable<InsuredObjectStub> e : SubPolicyInsuredObjectsList.this){
					if(object != null && object.id != null && object.id.equalsIgnoreCase(e.getValue().id)){
						e.setValue(object);
						break;
					}
				}
			}

			@Override
			public void removeInsuredObject(String id) {
				for(ValueSelectable<InsuredObjectStub> e : SubPolicyInsuredObjectsList.this){
					if(id != null && id.equalsIgnoreCase(e.getValue().id)){
						remove(e);
						break;
					}
				}
			}

			@Override
			public void addInsuredObject(InsuredObject object) {
//				String objectOwnerId = subPolicyBroker.getFinalMapping(object.ownerId);
//				String currentOwnerId = subPolicyBroker.getFinalMapping(SubPolicyInsuredObjectsList.this.ownerId);
//
//				if(object != null && objectOwnerId != null && ownerId != null && objectOwnerId.equalsIgnoreCase(currentOwnerId)){
//					SubPolicyInsuredObjectsList.this.addEntry(object);
//				}
			}

			@Override
			public void remapItemId(String newId, String Id) {
				return;
			}
		};
	}

	@Override
	protected void onAttach() {
		clearSelection();
		super.onAttach();
	}
}
