package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.presenter.InsurancePolicySelectionViewPresenter;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiptTransferToPolicyForm extends FormView<String> {

	protected ExpandableSelectionFormField policy;
	
	public ReceiptTransferToPolicyForm(){
		InsurancePolicySelectionViewPresenter selectionPanel = (InsurancePolicySelectionViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("RECEIPT_POLICY_SELECTION");
		selectionPanel.setOperationId(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RECEIPT);
		selectionPanel.go();
		policy = new ExpandableSelectionFormField(BigBangConstants.EntityIds.INSURANCE_POLICY, "Transferir para a Apólice", selectionPanel);
		
		addSection("Transferência de Apólice");
		addFormField(policy);
		
		setValidator(new ReceiptTransferToPolicyFormValidator(this));
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
