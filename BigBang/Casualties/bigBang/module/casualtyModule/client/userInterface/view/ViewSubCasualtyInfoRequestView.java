package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.userInterface.view.ViewInfoOrDocumentRequestView;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyForm;

public class ViewSubCasualtyInfoRequestView extends ViewInfoOrDocumentRequestView<SubCasualty>{

	public ViewSubCasualtyInfoRequestView(){
		super(new SubCasualtyForm());
	}
	
}
