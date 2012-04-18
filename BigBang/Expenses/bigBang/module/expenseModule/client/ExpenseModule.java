package bigBang.module.expenseModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.expenseModule.client.dataAccess.ExpenseBrokerImpl;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseOperationsViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseSearchOperationViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseSectionViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.MassParticipateToInsurerViewPresenter;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseOperationsView;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseSearchOperationView;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseSectionView;
import bigBang.module.expenseModule.client.userInterface.view.MassParticipateToInsurerView;

import com.google.gwt.core.client.GWT;

public class ExpenseModule implements Module {

	private boolean initialized = false;

	public void initialize() {
		registerViewPresenters();
		initialized = true;
	}

	public boolean isInitialized() {
		return initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("EXPENSE_SECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExpenseSectionView expenseSectionView = (ExpenseSectionView) GWT.create(ExpenseSectionView.class);
				ExpenseSectionViewPresenter expenseSectionViewPresenter = new ExpenseSectionViewPresenter(expenseSectionView);
				return expenseSectionViewPresenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("EXPENSE_OPERATIONS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExpenseOperationsView view = (ExpenseOperationsView) GWT.create(ExpenseOperationsView.class);
				ViewPresenter presenter = new ExpenseOperationsViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("EXPENSE_SEARCH", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExpenseSearchOperationView expenseSearchOperationView = (ExpenseSearchOperationView) GWT.create(ExpenseSearchOperationView.class);
				ExpenseSearchOperationViewPresenter expenseSearchOperationViewPresenter = new ExpenseSearchOperationViewPresenter(expenseSearchOperationView);
				return expenseSearchOperationViewPresenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_PARTICIPATE_TO_INSURER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				MassParticipateToInsurerView view = (MassParticipateToInsurerView) GWT.create(MassParticipateToInsurerView.class);
				MassParticipateToInsurerViewPresenter presenter = new MassParticipateToInsurerViewPresenter(view);
				return presenter;
			}
		});
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker<?>[]{
				new ExpenseBrokerImpl()
				};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				BigBangConstants.EntityIds.EXPENSE
		};
	}

}
