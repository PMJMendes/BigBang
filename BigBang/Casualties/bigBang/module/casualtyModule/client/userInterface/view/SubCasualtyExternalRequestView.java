package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.userInterface.view.ExternalRequestView;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyForm;

public class SubCasualtyExternalRequestView extends ExternalRequestView<SubCasualty>{

	public SubCasualtyExternalRequestView(){
		super(new SubCasualtyForm());
		
		setParentHeaderTitle("Ficha do Sub-Sinistro");
		
	}

	@Override
	public void setParentHeaderTitle(String title) {
		ownerHeader.setText(title);
	}

	@Override
	protected void initializeView() {
		return;
	}
	
	
	
}
