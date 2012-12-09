package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewExternalRequestViewPresenter;

public class ViewSubCasualtyExternalInfoRequestViewPresenter extends ViewExternalRequestViewPresenter<SubCasualty>{

	private SubCasualtyDataBroker broker;
	
	public ViewSubCasualtyExternalInfoRequestViewPresenter(Display<SubCasualty> view){
		super(view);
		broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
	}
	
	@Override
	protected void showOwner(String ownerId) {
		broker.getSubCasualty(ownerId, new ResponseHandler<SubCasualty>() {
			
			@Override
			public void onResponse(SubCasualty response) {
				view.getOwnerForm().setValue(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onFailure();
			}
		});
	}
	
	@Override
	protected void onFailure() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar a mensagem"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	protected void showHistory(String ownerId, String historyItemId) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "history");
		item.setParameter("historyownerid", ownerId);
		item.setParameter("historyitemid", historyItemId);
		NavigationHistoryManager.getInstance().go(item);		
	}
	
}
