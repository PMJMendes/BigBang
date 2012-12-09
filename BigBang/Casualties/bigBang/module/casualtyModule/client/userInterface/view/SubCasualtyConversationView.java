package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyForm;

public class SubCasualtyConversationView extends ConversationView<SubCasualty>{

	public SubCasualtyConversationView() {
		super(new SubCasualtyForm());
	}

}
