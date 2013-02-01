package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Receipt;
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

public class CreateReceiptViewPresenter implements ViewPresenter {

	public static enum Action {
		SAVE,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<Receipt> getForm();
		HasValue<InsurancePolicy> getParentForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
	}

	protected boolean bound = false;
	protected Display view;
	protected InsurancePolicyBroker broker;

	public CreateReceiptViewPresenter(Display view){
		setView((UIObject) view);
		broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
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
		String ownerId = parameterHolder.getParameter("policyid");
		if(ownerId == null || ownerId.isEmpty()){
			onFailure();
		}else{
			showCreateReceipt(ownerId);
		}
	}

	protected void bind(){
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<CreateReceiptViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case SAVE:
					onSave();
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});

		bound = true;
	}

	protected void showCreateReceipt(String ownerId){
		view.getForm().setValue(null);
		view.getParentForm().setValue(null);
		view.setSaveModeEnabled(true);
		broker.getPolicy(ownerId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				view.getParentForm().setValue(response);
				Receipt receipt = new Receipt();
				receipt.policyId = response.id;
				receipt.policyNumber = response.number;
				receipt.categoryName = response.categoryName;
				receipt.categoryId = response.categoryId;
				receipt.lineName = response.lineName;
				receipt.lineId = response.lineName;
				receipt.subLineName = response.subLineName;
				receipt.subLineId=  response.subLineId;
				receipt.managerId = response.managerId;
				receipt.mediatorId = response.inheritMediatorId;
				receipt.clientName = response.clientName;
				receipt.clientNumber = response.clientNumber;
				receipt.clientId = response.clientId;
				//receipt.insurerName = response. TODO Colocar o insurer acronym no stub da apólice
				receipt.inheritEndDate = response.expirationDate;
				receipt.ownerTypeId = BigBangConstants.EntityIds.INSURANCE_POLICY;
				view.getForm().setValue(receipt);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onFailure();
			}
		});

	}

	protected void onSave(){
		if(view.getForm().validate()) {
			Receipt receipt = view.getForm().getInfo();
			broker.createReceipt(receipt.policyId, receipt, new ResponseHandler<Receipt>() {

				@Override
				public void onResponse(Receipt response) {
					onCreateReceiptSuccess();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onCreateReceiptFailed();
				}
			});
		}else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	protected void onCancel(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCreateReceiptSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Recibo foi criado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCreateReceiptFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o recibo"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o recibo"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

}
