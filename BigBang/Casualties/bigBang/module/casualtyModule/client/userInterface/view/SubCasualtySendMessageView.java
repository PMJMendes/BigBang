package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyForm;

public class SubCasualtySendMessageView extends SendMessageView<SubCasualty>{

	public SubCasualtySendMessageView() {
		super(new SubCasualtyForm());
	}

}
