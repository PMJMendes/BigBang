package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class InsurancePolicyTransferToClientViewPresenter implements ViewPresenter{


	public static enum Action {
		CONFIRM,
		CANCEL
	}

	public interface Display{

		HasValueSelectables<ClientStub> getList();
		HasEditableValue<Client> getForm();

		void allowTransferToClient(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		Widget asWidget();

	}

	private Display view;
	private boolean bound = false;
	private InsurancePolicyBroker broker;
	private ClientProcessBroker clientBroker;
	private String policyId;

	public InsurancePolicyTransferToClientViewPresenter(Display view){
		clientBroker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
		broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		setView((UIObject)view);
	}
	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	private void bind() {
		if(bound){return;}

		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {



			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ClientStub> selected = (ValueSelectable<ClientStub>) event.getFirstSelected();
				ClientStub client = selected == null ? null : selected.getValue();
				if(client == null) {
					view.getForm().setValue(null);
				}else{
					clientBroker.getClient(client.id, new ResponseHandler<Client>(){

						@Override
						public void onResponse(Client response) {
							view.getForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							view.getForm().setValue(null);
							view.getList().clearSelection();
						}
					});
				}
			}
		});

		view.getForm().addValueChangeHandler(new ValueChangeHandler<Client>() {

			@Override
			public void onValueChange(ValueChangeEvent<Client> event) {
				Client client = event.getValue();
				view.allowTransferToClient(client != null);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<InsurancePolicyTransferToClientViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onTransferToClient(view.getForm().getValue());
					break;
				case CANCEL:
					onTransferToClientCancelled();
					break;
				}
			}
		});

	}
	protected void onTransferToClient(Client value) {

		broker.transferToClient(policyId, value.id, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("show");
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Transferência de cliente com sucesso."), TYPE.TRAY_NOTIFICATION));

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível transferir a apólice."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	protected void onTransferToClientCancelled() {

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);

	}
	@Override
	public void setParameters(HasParameters parameterHolder) {
		policyId = parameterHolder.getParameter("policyid");
		clearView();
	}

	private void clearView(){
		view.getList().clearSelection();
		view.getForm().setValue(null);
	}

}
