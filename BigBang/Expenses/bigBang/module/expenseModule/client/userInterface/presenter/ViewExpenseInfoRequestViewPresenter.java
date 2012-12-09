package bigBang.module.expenseModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewInfoOrDocumentRequestViewPresenter;

public class ViewExpenseInfoRequestViewPresenter extends ViewInfoOrDocumentRequestViewPresenter<Expense>{

	private ExpenseDataBroker broker;
	
	public ViewExpenseInfoRequestViewPresenter(Display<Expense> view){
		super(view);
		broker = (ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE);
		
	}

	@Override
	protected void showParent(String parentId) {
		broker.getExpense(parentId, new ResponseHandler<Expense>() {
			
			@Override
			public void onResponse(Expense response) {
				view.getParentForm().setValue(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onFailure();
			}
		});
		
	}

	@Override
	protected void showHistory(String processId, String historyItemId) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "history");
		item.setParameter("historyownerid", processId);
		item.setParameter("historyitemid", historyItemId);
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	protected void onFailure() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar a mensagem"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	protected void onRepeatSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A mensagem foi reenviada com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	@Override
	protected void onRepeatFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível reenviar a mensagem"), TYPE.ALERT_NOTIFICATION));
	}
	
}
