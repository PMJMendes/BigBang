package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.insurancePolicyModule.client.userInterface.form.SubPolicyHeaderForm;

public class InsuranceSubPolicyConversationView extends ConversationView<SubPolicy>{

	public InsuranceSubPolicyConversationView() {
		super(new SubPolicyHeaderForm());
	}

}
