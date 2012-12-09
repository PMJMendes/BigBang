package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.insurancePolicyModule.client.userInterface.form.SubPolicyHeaderForm;

public class SubPolicySendMessageView extends
		SendMessageView<SubPolicy> {

	public SubPolicySendMessageView() {
		super(new SubPolicyHeaderForm());
	}
}
