package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.userInterface.view.ViewExternalRequestView;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyForm;

public class ViewSubCasualtyExternalInfoRequestView extends ViewExternalRequestView<SubCasualty>{

	public ViewSubCasualtyExternalInfoRequestView(){
		super(new SubCasualtyForm());
	}
	
}
