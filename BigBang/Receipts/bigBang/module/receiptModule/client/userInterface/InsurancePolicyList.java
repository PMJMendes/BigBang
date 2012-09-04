package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Policy2Stub;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;

public class InsurancePolicyList extends FilterableList<Policy2Stub>{
	
	public class Entry extends InsurancePolicySearchPanel.Entry{
		
		public Entry(Policy2Stub value){
			super(value);
		}
		
	}
	
	protected InsurancePolicyBroker broker;
	
	public InsurancePolicyList(){
		
		this.broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		showFilterField(false);
	
	}
	
	public void addEntry(Policy2Stub stub){
		this.add(new Entry(stub));
	}
	
	@Override
	protected void onAttach() {
		clearSelection();
		super.onAttach();
	}

}
