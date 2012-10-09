package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubCasualty;
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

public class SubCasualtyRejectCloseViewPresenter implements ViewPresenter {
	public static enum Action {
		REJECT_CLOSE,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<String> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected SubCasualtyDataBroker broker;
	protected Display view;
	protected boolean bound = false;
	protected String subCasualtyId;

	public SubCasualtyRejectCloseViewPresenter(Display view){
		this.broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		setView((UIObject)view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();

		subCasualtyId = parameterHolder.getParameter("subcasualtyid");
		if(subCasualtyId != null && !subCasualtyId.isEmpty()){
			showReject();
		}else{
			onRejectFailed();
		}
	}

	protected void bind() {
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case REJECT_CLOSE:
					onReject();
					break;
				case CANCEL:
					onCancel();
					break;
				}				
			}
		});

		bound = true;
	}

	protected void clearView(){
		view.getForm().setValue(null);
	}

	protected void showReject(){
		this.broker.getSubCasualty(subCasualtyId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				//				if(!PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.SubCasualtyProcess.REJECT_CLOSE_SUB_CASUALTY)){ TODO IMPORTANT FJVC
				//					onRejectFailed();
				//				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onRejectFailed();
			}
		});
	}

	protected void onReject(){
		this.broker.getSubCasualty(subCasualtyId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				if(view.getForm().validate()) {
					broker.rejectCloseSubCasualty(response.id, view.getForm().getInfo(), new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {
							onRejectSuccess();
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onRejectFailed();
						}
					});
				}
				else{
					onValidationFailed();
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onRejectFailed();
			}
		});
	}
	
	private void onValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));				
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onRejectFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Rejeitar o Encerramento"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onRejectSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Encerramento de Sub-Sinistro Rejeitado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		item.removeParameter("subcasualtyid");
		NavigationHistoryManager.getInstance().go(item);
	}

}
