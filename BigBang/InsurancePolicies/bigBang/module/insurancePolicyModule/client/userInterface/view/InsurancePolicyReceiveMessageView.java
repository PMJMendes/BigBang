package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.insurancePolicyModule.client.userInterface.form.InsurancePolicyHeaderForm;

public class InsurancePolicyReceiveMessageView extends ReceiveMessageView<InsurancePolicy>{

	public InsurancePolicyReceiveMessageView() {
		super(new InsurancePolicyHeaderForm());
	}

}
