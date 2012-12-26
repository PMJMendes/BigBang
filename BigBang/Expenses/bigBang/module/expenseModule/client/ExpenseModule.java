package bigBang.module.expenseModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.expenseModule.client.dataAccess.ExpenseBrokerImpl;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseConversationViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseDeleteViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseReceiveMessageViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseSendMessageViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseOperationsViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseSearchOperationViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseSectionViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.MassNotifyResultsClientViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.MassParticipateToInsurerViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.MassReturnToClientViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ProofReceptionViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ReceiveAcceptanceViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ReceiveReturnViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.SerialExpenseCreationViewPresenter;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseConversationView;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseDeleteView;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseReceiveMessageView;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseSendMessageView;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseOperationsView;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseSearchOperationView;
import bigBang.module.expenseModule.client.userInterface.view.ExpenseSectionView;
import bigBang.module.expenseModule.client.userInterface.view.MassNotifyResultsClientView;
import bigBang.module.expenseModule.client.userInterface.view.MassParticipateToInsurerView;
import bigBang.module.expenseModule.client.userInterface.view.MassReturnToClientView;
import bigBang.module.expenseModule.client.userInterface.view.ProofReceptionView;
import bigBang.module.expenseModule.client.userInterface.view.ReceiveAcceptanceView;
import bigBang.module.expenseModule.client.userInterface.view.ReceiveReturnView;
import bigBang.module.expenseModule.client.userInterface.view.SerialExpenseCreationView;
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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("SERIAL_EXPENSE_CREATION", new ViewPresenterInstantiator(){
			@Override
			public ViewPresenter getInstance() {
				SerialExpenseCreationView view = (SerialExpenseCreationView) GWT.create(SerialExpenseCreationView.class);
				SerialExpenseCreationViewPresenter presenter = new SerialExpenseCreationViewPresenter(view);
				return presenter;
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
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_NOTIFY_RESULTS_CLIENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				MassNotifyResultsClientView view = (MassNotifyResultsClientView) GWT.create(MassNotifyResultsClientView.class);
				MassNotifyResultsClientViewPresenter presenter = new MassNotifyResultsClientViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIVE_ACCEPTANCE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ReceiveAcceptanceView view = (ReceiveAcceptanceView) GWT.create(ReceiveAcceptanceView.class);
				ReceiveAcceptanceViewPresenter presenter = new ReceiveAcceptanceViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RECEIVE_RETURN", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ReceiveReturnView view = (ReceiveReturnView) GWT.create(ReceiveReturnView.class);
				ReceiveReturnViewPresenter presenter = new ReceiveReturnViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("EXPENSE_DELETE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExpenseDeleteView view = (ExpenseDeleteView) GWT.create(ExpenseDeleteView.class);
				ExpenseDeleteViewPresenter presenter = new ExpenseDeleteViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("EXPENSE_SEND_MESSAGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExpenseSendMessageView view = (ExpenseSendMessageView) GWT.create(ExpenseSendMessageView.class);
				ExpenseSendMessageViewPresenter presenter = new ExpenseSendMessageViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("EXPENSE_RECEIVE_MESSAGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExpenseReceiveMessageView view = (ExpenseReceiveMessageView) GWT.create(ExpenseReceiveMessageView.class);
				ExpenseReceiveMessageViewPresenter presenter = new ExpenseReceiveMessageViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MASS_RETURN_TO_CLIENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				MassReturnToClientView view = (MassReturnToClientView) GWT.create(MassReturnToClientView.class);
				MassReturnToClientViewPresenter presenter = new MassReturnToClientViewPresenter(view);
				return presenter;
			}
		});		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("PROOF_RECEPTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ProofReceptionView view = (ProofReceptionView) GWT.create(ProofReceptionView.class);
				ViewPresenter presenter = new ProofReceptionViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("EXPENSE_CONVERSATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExpenseConversationView view = (ExpenseConversationView) GWT.create(ExpenseConversationView.class);
				ExpenseConversationViewPresenter presenter = new ExpenseConversationViewPresenter(view);
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
