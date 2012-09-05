package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.userInterface.view.ViewInfoOrDocumentRequestView;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm_OLD;


public class ViewInsurancePolicyInfoRequestView extends ViewInfoOrDocumentRequestView<InsurancePolicy>{

	public ViewInsurancePolicyInfoRequestView(){
		super(new InsurancePolicyForm_OLD() {
			
			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
		});
	}
	
}
