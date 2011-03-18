package bigBang.module.generalSystemModule.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;

import bigBang.library.client.EventBus;
import bigBang.library.interfaces.Service;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.TextBadge;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.view.CostCenterManagementOperationView;
import bigBang.module.generalSystemModule.interfaces.CostCenterService;
import bigBang.module.generalSystemModule.interfaces.CostCenterServiceAsync;
import bigBang.module.generalSystemModule.shared.CostCenterManagementOperation;

public class GeneralSystemSection implements MenuSection {

	private static final String ID = "GENERAL_SYSTEM_SECTION";
	private static final String DESCRIPTION = "Sistema Geral";
	private static final String SHORT_DESCRIPTION = "Sist. Geral";
	private HashMap<String, OperationViewPresenter> sectionOperationPresenters; 
	
	public GeneralSystemSection(){
		this.sectionOperationPresenters = new HashMap<String, OperationViewPresenter>();

		/* COST CENTER MANAGEMENT */
		CostCenterManagementOperation costCenterManagementOperation = (CostCenterManagementOperation)GWT.create(CostCenterManagementOperation.class);
		CostCenterManagementOperationView costCenterManagementOperationView = new CostCenterManagementOperationView();
		CostCenterServiceAsync costCenterService = CostCenterService.Util.getInstance();
		CostCenterManagementOperationViewPresenter costCenterManagementOperationPresenter = new CostCenterManagementOperationViewPresenter(null, (Service)costCenterService, costCenterManagementOperationView);
		costCenterManagementOperationPresenter.setOperation(costCenterManagementOperation);
		this.sectionOperationPresenters.put(CostCenterManagementOperation.ID, (OperationViewPresenter)costCenterManagementOperationPresenter);

		/* CLIENT GROUP MANAGEMENT */

		/* MEDIATORS MANAGEMENT */
		
		/* INSURANCE AGENCIES MANAGEMENT */
		
		/* USERS MANAGEMENT */
		
		/* TAXES AND MULTIPLIERS MANAGEMENT */
		
		/* COVERAGES MANAGEMENT */
		
	}
	
	public String getId() {
		return ID;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getShortDescription() {
		return SHORT_DESCRIPTION;
	}

	public TextBadge getBadge() {
		return null;
	}

	public boolean hasBadge() {
		return false;
	}
	
	public OperationViewPresenter[] getOperationPresenters(){
		int nOps = this.sectionOperationPresenters.size();
		OperationViewPresenter[] result = new OperationViewPresenter[nOps];
		for(int i = 0; i < nOps; i++)
			result[i] = (OperationViewPresenter)this.sectionOperationPresenters.values().toArray()[i];
		return result;
	}

	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

}
