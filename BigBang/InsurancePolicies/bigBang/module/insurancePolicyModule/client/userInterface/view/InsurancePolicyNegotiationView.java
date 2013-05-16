package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.insurancePolicyModule.client.userInterface.form.InsurancePolicyForm;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationView;

public class InsurancePolicyNegotiationView<T> extends NegotiationView<T> {

	
	@SuppressWarnings("unchecked")
	public InsurancePolicyNegotiationView() {
		super((FormView<T>) new InsurancePolicyForm());
		getForm().setInsurancePolicyLocked(true);
		setParentHeaderTitle("Ficha da Apólice");
	}
}
