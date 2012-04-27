package bigBang.module.expenseModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.Expense.ReturnEx;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ReceiveReturnViewPresenter implements ViewPresenter{


	public static enum Action{
		CANCEL,
		ACCEPT,
	}

	protected Display view;
	protected boolean bound = false;
	private ExpenseDataBroker broker;

	public interface Display{
		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void clearForms();
		HasEditableValue<ReturnEx> getForm();
	}

	public ReceiveReturnViewPresenter(Display view){
		setView((UIObject)view);
		broker = (ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	private void bind() {
		if(bound){return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case ACCEPT:
					onAccept();
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});
		
		bound = true;
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onAccept() {
		ReturnEx returnEx = view.getForm().getInfo();
		broker.receiveReturn(returnEx, new ResponseHandler<Expense>() {
			
			@Override
			public void onResponse(Expense response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("show");
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Devolução recebida com sucesso."), TYPE.TRAY_NOTIFICATION));
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível receber a Devolução."), TYPE.ALERT_NOTIFICATION));
				
			}
		});
		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		view.clearForms();
		ReturnEx reject = new ReturnEx();
		reject.expenseId = parameterHolder.getParameter("expenseid");
		view.getForm().setValue(reject);
	}


}
