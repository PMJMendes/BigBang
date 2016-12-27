package bigBang.module.expenseModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Expense;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;

public class ExpenseReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<Expense>{

	private ExpenseDataBroker broker;

	public ExpenseReceiveMessageViewPresenter(Display<Expense> view) {
		super(view);
		broker = (ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE);
	}
	
	@Override
	protected void showOwner(String ownerId) {
		broker.getExpense(ownerId, new ResponseHandler<Expense>() {

			@Override
			public void onResponse(Expense response) {
				view.getOwnerForm().setValue(response);	
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar a despesa de saúde."), TYPE.ALERT_NOTIFICATION));

			}
		});		
	}
	@Override
	protected void receive() {
		broker.receiveMessage(view.getForm().getInfo(), new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				onReceiveMessageSuccess();
				navigateBack();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				String error = "";
				for (ResponseError tmp : errors) {
					error = error + tmp.description;
				}
				onReceiveMessageFailed(error);;
			}
		});		
	}
}
