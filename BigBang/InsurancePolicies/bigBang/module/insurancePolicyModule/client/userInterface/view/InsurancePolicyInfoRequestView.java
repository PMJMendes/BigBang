package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.Policy2;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestView;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;

public class InsurancePolicyInfoRequestView extends
		InfoOrDocumentRequestView<Policy2> {

	public InsurancePolicyInfoRequestView() {
		super(new InsurancePolicyForm() {
			
			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
		});
	}

}
