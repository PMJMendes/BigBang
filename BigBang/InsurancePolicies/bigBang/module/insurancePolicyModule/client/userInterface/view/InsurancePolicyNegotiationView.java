package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationView;

public class InsurancePolicyNegotiationView<T> extends NegotiationView<T>{

	
	@SuppressWarnings("unchecked")
	public InsurancePolicyNegotiationView() {
		super((FormView<T>) new InsurancePolicyForm(){
			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
			
		});	
	}
}
