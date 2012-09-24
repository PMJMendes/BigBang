package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.userInterface.view.ViewInfoOrDocumentRequestView;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyForm;

public class ViewSubPolicyInfoRequestView extends ViewInfoOrDocumentRequestView<SubPolicy>{

	public ViewSubPolicyInfoRequestView(){
		super(new SubPolicyForm());
	}
	
}
