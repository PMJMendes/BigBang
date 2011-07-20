package bigBang.module.expenseModule.client;

import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.Process;
import bigBang.library.client.event.ModuleInitializedEvent;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseSectionViewPresenter;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseSectionView;

public class ExpenseModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	protected BigBangPermissionManager permissionManager;

	public ExpenseModule(){
	}

	public void initialize(EventBus eventBus, BigBangPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
		initialize(eventBus); 
	}

	public void initialize(EventBus eventBus) {
		sectionPresenters = new SectionViewPresenter[1];

		//Expense section
		ExpenseSection expenseSection = new ExpenseSection(this.permissionManager);
		ExpenseSectionView expenseSectionView = new ExpenseSectionView();
		ExpenseSectionViewPresenter expenseSectionViewPresenter = new ExpenseSectionViewPresenter(eventBus, null, expenseSectionView);
		expenseSectionViewPresenter.setSection(expenseSection);
		expenseSection.registerEventHandlers(eventBus);
		sectionPresenters[0] = expenseSectionViewPresenter;

		eventBus.fireEvent(new ModuleInitializedEvent(this));
	}

	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	public SectionViewPresenter[] getMainMenuSectionPresenters() {
		return sectionPresenters;
	}

	public Process[] getProcesses() {
		return null;
	}

}
