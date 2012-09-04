package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.Policy2;
import bigBang.library.client.userInterface.view.ViewInfoOrDocumentRequestView;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;


public class ViewInsurancePolicyInfoRequestView extends ViewInfoOrDocumentRequestView<Policy2>{

	public ViewInsurancePolicyInfoRequestView(){
		super(new InsurancePolicyForm() {
			
			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
		});
	}
	
}
