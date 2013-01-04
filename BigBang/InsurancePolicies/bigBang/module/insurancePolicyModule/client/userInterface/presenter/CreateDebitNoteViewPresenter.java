package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class CreateDebitNoteViewPresenter implements ViewPresenter {

	public static enum Action {
		CREATE,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<DebitNote> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	private Display view;
	private InsurancePolicyBroker broker;
	private boolean bound = false;
	private String ownerId;

	public CreateDebitNoteViewPresenter(Display view) {
		this.broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		setView((UIObject) view);
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
		ownerId = parameterHolder.getParameter("policyid");

		if(ownerId == null){
			onGetOwnerFailed();
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.removeParameter("show");
			NavigationHistoryManager.getInstance().go(item);
		}else{
			view.getForm().setValue(null);
		}
	}

	private void bind(){
		if(bound){return;}

		view.registerActionHandler(new ActionInvokedEventHandler<CreateDebitNoteViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CREATE:
					onCreateDebitNote();
					break;
				case CANCEL:
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("show");
					NavigationHistoryManager.getInstance().go(item);
					break;
				}
			}
		});
		bound = true;
	}

	private void onCreateDebitNote(){
		if(view.getForm().validate()) {
			final DebitNote note = view.getForm().getInfo();
			broker.getPolicy(ownerId, new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					boolean hasPermission = PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_DEBIT_NOTE);
					if(hasPermission){
						broker.issueDebitNote(ownerId, note, new ResponseHandler<Void>() {

							@Override
							public void onResponse(Void response) {
								onCreateDebitNoteSuccess();
								NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
								item.removeParameter("show");
								NavigationHistoryManager.getInstance().go(item);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								onCreateDebitNoteFailed();
							}
						});
					}else{
						onCreateDebitNoteFailed();
						NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
						item.removeParameter("show");
						NavigationHistoryManager.getInstance().go(item);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("show");
					NavigationHistoryManager.getInstance().go(item);
				}
			});
		}else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	private void onGetOwnerFailed(){
		onCreateDebitNoteFailed();
	}

	private void onCreateDebitNoteSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Nota de Débito foi criada com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onCreateDebitNoteFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a nota de débito"), TYPE.ALERT_NOTIFICATION));
	}

}
