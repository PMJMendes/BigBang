package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewInfoOrDocumentRequestViewPresenter;

public class ViewInsurancePolicyInfoRequestViewPresenter extends ViewInfoOrDocumentRequestViewPresenter<InsurancePolicy>{

	private InsurancePolicyBroker broker;
	
	public ViewInsurancePolicyInfoRequestViewPresenter(Display<InsurancePolicy> view){
		super(view);
		broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		
	}

	@Override
	protected void showParent(String parentId) {
		broker.getPolicy(parentId, new ResponseHandler<InsurancePolicy>() {
			
			@Override
			public void onResponse(InsurancePolicy response) {
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
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresenter o Pedido de Informação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	protected void onRepeatSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Pedido de Informação foi Repetido com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	@Override
	protected void onRepeatFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Repetir o Pedido de Informação"), TYPE.ALERT_NOTIFICATION));
	}
	
}
