package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.insurancePolicyModule.client.userInterface.form.SubPolicyHeaderForm;

public class SubPolicyReceiveMessageView extends ReceiveMessageView<SubPolicy>{

	public SubPolicyReceiveMessageView() {
		super(new SubPolicyHeaderForm());
	}

}
