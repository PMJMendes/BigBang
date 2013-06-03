package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;

public class InsurancePolicyList extends FilterableList<InsurancePolicyStub> {
	
	public class Entry extends InsurancePolicySearchPanel.Entry{
		
		public Entry(InsurancePolicyStub value){
			super(value);
		}
		
	}
	
	protected InsurancePolicyBroker broker;
	
	public InsurancePolicyList(){
		
		this.broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		showFilterField(false);
	
	}
	
	public void addEntry(InsurancePolicyStub stub){
		this.add(new Entry(stub));
	}
	
	@Override
	protected void onAttach() {
		clearSelection();
		super.onAttach();
	}

}
