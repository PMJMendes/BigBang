package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestView;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;

public class InsurancePolicyInfoRequestView extends
		InfoOrDocumentRequestView<InsurancePolicy> {

	public InsurancePolicyInfoRequestView() {
		super(new InsurancePolicyForm());
	}


}
