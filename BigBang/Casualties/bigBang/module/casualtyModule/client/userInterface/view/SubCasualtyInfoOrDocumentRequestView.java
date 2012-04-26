package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestView;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyForm;

public class SubCasualtyInfoOrDocumentRequestView extends InfoOrDocumentRequestView<SubCasualty>{

	public SubCasualtyInfoOrDocumentRequestView() {
		super(new SubCasualtyForm());
	}

}
