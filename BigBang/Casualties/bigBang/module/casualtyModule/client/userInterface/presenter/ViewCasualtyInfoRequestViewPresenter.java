package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewInfoOrDocumentRequestViewPresenter;

public class ViewCasualtyInfoRequestViewPresenter extends ViewInfoOrDocumentRequestViewPresenter<Casualty>{

	private CasualtyDataBroker broker;
	
	public ViewCasualtyInfoRequestViewPresenter(Display<Casualty> view){
		super(view);
		broker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
	}
	
	@Override
	protected void showParent(String parentId) {
		broker.getCasualty(parentId, new ResponseHandler<Casualty>() {
			
			@Override
			public void onResponse(Casualty response) {
				view.getParentForm().setValue(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onFailure();
			}
		});	
		
	};
	
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
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar a mensagem"), TYPE.ALERT_NOTIFICATION));
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
