package bigBang.module.expenseModule.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.MenuSections;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.TextBadge;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseSearchOperationViewPresenter;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseSearchOperationView;
import bigBang.module.expenseModule.interfaces.ExpenseService;
import bigBang.module.expenseModule.interfaces.ExpenseServiceAsync;
import bigBang.module.expenseModule.shared.ModuleConstants;
import bigBang.module.expenseModule.shared.operation.ExpenseSearchOperation;

public class ExpenseSection implements MenuSection {

	private static final String ID = ModuleConstants.ProcessTypeIDs.EXPENSE;
	private static final String DESCRIPTION = "Despesa de Saúde";
	private static final String SHORT_DESCRIPTION = "Desp. Saúde";
	private ArrayList<OperationViewPresenter> sectionOperationPresenters;
	public BigBangPermissionManager permissionManager;
	
	public ExpenseSection(BigBangPermissionManager permissionManager){
		this.sectionOperationPresenters = new ArrayList<OperationViewPresenter>();
		this.permissionManager = permissionManager;

		ExpenseSearchOperation expenseSearchOperation = (ExpenseSearchOperation)GWT.create(ExpenseSearchOperation.class);
		ExpenseSearchOperationView expenseSearchOperationView = (ExpenseSearchOperationView) GWT.create(ExpenseSearchOperationView.class);
		ExpenseServiceAsync expenseService = ExpenseService.Util.getInstance();
		ExpenseSearchOperationViewPresenter expenseSearchOperationViewPresenter = new ExpenseSearchOperationViewPresenter(null, expenseService, expenseSearchOperationView);
		expenseSearchOperationViewPresenter.setOperation((Operation) expenseSearchOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)expenseSearchOperationViewPresenter);
	}
	
	@Override
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
		OperationViewPresenter[] result = new OperationViewPresenter[this.sectionOperationPresenters.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = this.sectionOperationPresenters.get(i);
		}
		return result;
	}

	public void registerEventHandlers(EventBus eventBus) {
		for(ViewPresenter p : this.getOperationPresenters()) {
			p.setEventBus(eventBus);
		}
	}

	@Override
	public MenuSections getMenuIndex() {
		return MenuSections.EXPENSE_SECTION;
	}

}
