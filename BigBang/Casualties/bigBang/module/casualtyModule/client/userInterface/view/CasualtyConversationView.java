package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.Casualty;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.casualtyModule.client.userInterface.form.CasualtyForm;

public class CasualtyConversationView extends ConversationView<Casualty>{

	public CasualtyConversationView() {
		super(new CasualtyForm());
	}

}
