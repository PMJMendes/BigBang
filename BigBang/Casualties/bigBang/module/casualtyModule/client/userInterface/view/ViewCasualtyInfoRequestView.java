package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.Casualty;
import bigBang.library.client.userInterface.view.ViewInfoOrDocumentRequestView;
import bigBang.module.casualtyModule.client.userInterface.form.CasualtyForm;

public class ViewCasualtyInfoRequestView extends ViewInfoOrDocumentRequestView<Casualty>{
	
	public ViewCasualtyInfoRequestView(){
		super(new CasualtyForm());
	}

}
