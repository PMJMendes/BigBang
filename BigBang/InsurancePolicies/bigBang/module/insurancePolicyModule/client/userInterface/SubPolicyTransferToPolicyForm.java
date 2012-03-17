package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyMainPolicySelectionViewPresenter;

public class SubPolicyTransferToPolicyForm extends FormView<String> {

	protected ExpandableSelectionFormField policy;
	
	public SubPolicyTransferToPolicyForm(){
		SubPolicyMainPolicySelectionViewPresenter selectionPanel = (SubPolicyMainPolicySelectionViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("INSURANCE_POLICY_SUB_POLICY_MAIN_POLICY_SELECTION");
		selectionPanel.go();
		policy = new ExpandableSelectionFormField(BigBangConstants.EntityIds.INSURANCE_POLICY, "Apólice Principal", selectionPanel);
		
		addSection("Transferência para Apólice Principal");
		addFormField(policy);
	}
	
	@Override
	public String getInfo() {
		return policy.getValue();
	}

	@Override
	public void setInfo(String info) {
		policy.setValue(info);
	}

}
