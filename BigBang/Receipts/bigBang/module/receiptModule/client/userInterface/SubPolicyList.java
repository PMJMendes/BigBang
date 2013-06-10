package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicySearchPanel;

public class SubPolicyList  extends FilterableList<SubPolicyStub> {
	
	public class Entry extends SubPolicySearchPanel.Entry{
		
		public Entry(SubPolicyStub value){
			super(value);
		}
		
	}
	
	protected InsuranceSubPolicyBroker broker;
	
	public SubPolicyList(){
		
		this.broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		showFilterField(false);
	
	}
	
	public void addEntry(SubPolicyStub stub){
		this.add(new Entry(stub));
	}
	
	@Override
	protected void onAttach() {
		clearSelection();
		super.onAttach();
	}

}
