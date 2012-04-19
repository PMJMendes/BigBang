package bigBang.module.expenseModule.client.userInterface;

import com.google.gwt.user.client.ui.StackPanel;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.dataAccess.ExpenseDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;

public class ExpenseChildrenPanel extends View{

	protected Expense expense;
	protected ExpenseDataBrokerClient expenseBrokerClient;
	
	public ContactsList contactsList;
	public DocumentsList documentsList;
	public HistoryList historyList;
	public SubProcessesList subProcessesList;

	public ExpenseChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		subProcessesList = new SubProcessesList();
		historyList = new HistoryList();
		
		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		wrapper.add(subProcessesList, "Sub-Processos");
		wrapper.add(historyList, "Histórico");
		
		expenseBrokerClient = getExpenseBrokerClient();
		((ExpenseDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.EXPENSE)).registerClient(expenseBrokerClient);
	}
	
	private ExpenseDataBrokerClient getExpenseBrokerClient() {
		return new ExpenseDataBrokerClient() {
			
			protected int version;
			
			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){
					this.version = number;
				}
			}
			
			@Override
			public int getDataVersion(String dataElementId) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){
					return this.version;
				}
				return -1;
			}
			
			@Override
			public void updateExpense(Expense expense) {
				return;
			}
			
			@Override
			public void deleteExpense(String id) {
				if(expense != null && expense.id != null && id.equalsIgnoreCase(expense.id)){
					setExpense(null);
				}
			}
			
			@Override
			public void addExpense(Expense expense) {
				return;
			}
		};
	}
	
	public void setExpense(Expense expense) {
		this.expense = expense;
		String expenseId = expense == null ? null: expense.id;
		
		boolean allow = expense != null ? PermissionChecker.hasPermission(expense, BigBangConstants.OperationIds.ExpenseProcess.UPDATE_EXPENSE) : false;

		contactsList.setOwner(expenseId);
		contactsList.setOwnerType(BigBangConstants.EntityIds.EXPENSE);
		contactsList.allowCreation(allow);
		documentsList.setOwner(expenseId);
		documentsList.setOwnerType(BigBangConstants.EntityIds.EXPENSE);
		documentsList.allowCreation(allow);
		subProcessesList.setOwner(expenseId);
		historyList.setOwner(expenseId);
	}

	@Override
	protected void initializeView() {
		return;
	}



}
