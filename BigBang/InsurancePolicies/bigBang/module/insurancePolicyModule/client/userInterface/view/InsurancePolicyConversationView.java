package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.insurancePolicyModule.client.userInterface.form.InsurancePolicyHeaderForm;

public class InsurancePolicyConversationView extends ConversationView<InsurancePolicy>{

	public InsurancePolicyConversationView() {
		super(new InsurancePolicyHeaderForm());
	}

}
