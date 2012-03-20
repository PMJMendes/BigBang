package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.presenter.InsurancePolicySelectionViewPresenter;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiptTransferToPolicyForm extends FormView<String> {

	protected ExpandableSelectionFormField policy;
	
	public ReceiptTransferToPolicyForm(){
		InsurancePolicySelectionViewPresenter selectionPanel = (InsurancePolicySelectionViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("RECEIPT_POLICY_SELECTION");
		selectionPanel.setOperationId(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_SUB_POLICY);
		selectionPanel.go();
		policy = new ExpandableSelectionFormField(BigBangConstants.EntityIds.INSURANCE_POLICY, "Apólice Principal", selectionPanel);
		
		addSection("Transferência para Apólice");
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
