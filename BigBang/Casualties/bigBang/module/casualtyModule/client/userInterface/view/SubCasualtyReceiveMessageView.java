package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyForm;

public class SubCasualtyReceiveMessageView extends ReceiveMessageView<SubCasualty>{

	public SubCasualtyReceiveMessageView(){
		super(new SubCasualtyForm());
	}

}
