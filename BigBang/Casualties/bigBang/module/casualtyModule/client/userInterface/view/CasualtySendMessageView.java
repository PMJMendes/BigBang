package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.Casualty;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.casualtyModule.client.userInterface.form.CasualtyForm;

public class CasualtySendMessageView extends SendMessageView<Casualty>{

	public CasualtySendMessageView() {
		super(new CasualtyForm());
	}
}
