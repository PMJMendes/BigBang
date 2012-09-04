package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestView;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyForm;

public class SubPolicyClientInfoOrDocumentRequestView extends
		InfoOrDocumentRequestView<SubPolicy> {

	public SubPolicyClientInfoOrDocumentRequestView() {
		super(new SubPolicyForm());
	}
		
}
