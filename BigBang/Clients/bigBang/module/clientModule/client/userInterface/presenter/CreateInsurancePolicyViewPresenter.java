package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.EventBus;
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

public class CreateInsurancePolicyViewPresenter implements ViewPresenter {

	public static enum Action {
		CONFIRM,
		CANCEL
	}

	public static interface Display {
		String getCategory();
		String getLine();
		String getSubLine();

		void clear();

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		Widget asWidget();
	}

	protected boolean bound = false;
	protected Display view;
	protected InsurancePolicyBroker broker;
	protected String clientId;

	public CreateInsurancePolicyViewPresenter(Display view) {
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
		view.clear();

		this.clientId = parameterHolder.getParameter("clientid");

		if(this.clientId == null || this.clientId.isEmpty()) {
			onError();
		}
	}

	protected void bind(){
		if(bound) {
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<CreateInsurancePolicyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case CONFIRM:
					onConfirm();
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});

		bound = true;
	}

	protected void onConfirm(){
		String categoryId = view.getCategory();
		String lineId = view.getLine();
		String subLineId = view.getSubLine();

		if(categoryId == null || lineId == null || subLineId == null) {
			onConfirmError();
		}else{
			broker.openPolicyResource(null, new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					response.clientId = clientId;
					response.categoryId = view.getCategory();
					response.lineId = view.getLine();
					response.subLineId = view.getSubLine();
//					response.statusIcon = PolicyStatus.PROVISIONAL;
					
					broker.initPolicy(response, new ResponseHandler<InsurancePolicy>() {

						@Override
						public void onResponse(InsurancePolicy response) {
							onSuccess(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							CreateInsurancePolicyViewPresenter.this.onError();
						}
					});				
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					CreateInsurancePolicyViewPresenter.this.onError();
				}
			});
		}
	}

	protected void onSuccess(InsurancePolicy policy){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Apólice em criação no espaço de trabalho"), TYPE.TRAY_NOTIFICATION));

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);

		item = new NavigationHistoryItem();
		item.setParameter("section", "insurancepolicy");
		item.setStackParameter("display");
		item.pushIntoStackParameter("display", "search");
		item.setParameter("policyid", policy.id);
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCancel(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onError(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível  criar a Apólice"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onConfirmError(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Os campos de Categoria, Ramo e Modalidade são necessários para a criação da Apólice"), TYPE.ALERT_NOTIFICATION));
	}
	
}
