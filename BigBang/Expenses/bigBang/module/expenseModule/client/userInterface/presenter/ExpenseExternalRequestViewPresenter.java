package bigBang.module.expenseModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ExternalRequestViewPresenter;

public class ExpenseExternalRequestViewPresenter extends ExternalRequestViewPresenter<Expense>{

	private ExpenseDataBroker broker;
	
	public ExpenseExternalRequestViewPresenter(Display<Expense> view) {
		super(view);
		broker = (ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE);
	}
	
	@Override
	public void setParameters(final HasParameters parameterHolder){
		ownerId = parameterHolder.getParameter("expenseid");
		ownerTypeId = BigBangConstants.EntityIds.EXPENSE;
		
	
		broker.getExpense(ownerId, new ResponseHandler<Expense>() {
			
			@Override
			public void onResponse(Expense response) {
				view.getOwnerForm().setValue(response);	
				setParentParameters(parameterHolder);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar a despesa de saúde."), TYPE.ALERT_NOTIFICATION));

			}
		});
	}
	
	protected void setParentParameters(HasParameters parameterHolder) {
		super.setParameters(parameterHolder);	
	}

	@Override
	protected void createExternalInfoRequest(ExternalInfoRequest toSend) {
		broker.createExternalInfoRequest(toSend, new ResponseHandler<ExternalInfoRequest>() {


			@Override
			public void onResponse(ExternalInfoRequest response) {

				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Pedido de Informação Externo guardado com sucesso."), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
				navig.popFromStackParameter("display");
				navig.removeParameter("externalrequestid");	
				NavigationHistoryManager.getInstance().go(navig);
//				counter = 0;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar o Pedido de Informação Externo."), TYPE.ALERT_NOTIFICATION));
//				counter = 0;
			}
		});	
		
	}
}
