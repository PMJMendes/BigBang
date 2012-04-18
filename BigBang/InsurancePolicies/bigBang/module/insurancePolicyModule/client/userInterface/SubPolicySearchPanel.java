package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.view.SearchPanel;

public class SubPolicySearchPanel extends SearchPanel<SubPolicyStub> {

	public SubPolicySearchPanel() {
		super(((InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)).getSearchBroker());
	}

	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResults(Collection<SubPolicyStub> results) {
		// TODO Auto-generated method stub
		
	}

}
