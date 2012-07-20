package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.view.FormViewSection;

public class MediatorCategoryFormSection extends FormViewSection {

	protected CoverageBroker broker;
	
	public MediatorCategoryFormSection(String categoryId) {
		super("");
		broker = (CoverageBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);
		setCategoryId(categoryId);
	}

	public void setCategoryId(String categoryId) {
//		broker.get
	}
	
}
