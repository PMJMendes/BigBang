package bigBang.module.expenseModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class ReceiveAcceptanceViewPresenter implements ViewPresenter{


	public static enum Action{
		CANCEL,
		ACCEPT,
		REFUSE
	}

	protected Display view;
	protected boolean bound = false;
	private ExpenseDataBroker broker;

	public interface Display{
		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		//TODO getForms, aceitaçao e rejeição VENTURA
		void clearForms();
	}

	public ReceiveAcceptanceViewPresenter(Display view){
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
		
		view.registerActionHandler(new ActionInvokedEventHandler<ReceiveAcceptanceViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case ACCEPT:
					onAccept();
					break;
				case CANCEL:
					onCancel();
					break;
				case REFUSE:
					onRefuse();
					break;
				}
			}
		});
		
		bound = true;
	}

	protected void onRefuse() {
		// TODO Auto-generated method stub
		
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onAccept() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		view.clearForms();
	}


}
