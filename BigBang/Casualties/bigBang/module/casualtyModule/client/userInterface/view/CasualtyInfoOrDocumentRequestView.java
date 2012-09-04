package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.Casualty;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestView;
import bigBang.module.casualtyModule.client.userInterface.CasualtyForm;

public class CasualtyInfoOrDocumentRequestView extends InfoOrDocumentRequestView<Casualty>{

	public CasualtyInfoOrDocumentRequestView() {
		super(new CasualtyForm());
	}

}
