package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.insurancePolicyModule.client.userInterface.form.InsurancePolicyHeaderForm;

public class InsurancePolicySendMessageView extends SendMessageView<InsurancePolicy>{

	public InsurancePolicySendMessageView() {
		super(new InsurancePolicyHeaderForm());
	}

}
