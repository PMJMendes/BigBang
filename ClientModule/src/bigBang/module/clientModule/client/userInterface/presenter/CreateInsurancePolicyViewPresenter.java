package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.Operation;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;

public abstract class CreateInsurancePolicyViewPresenter implements
		OperationViewPresenter {
	
	public enum Action {
		CREATE_POLICY,
		CANCEL_POLICY_CREATION
	}
	
	public interface Display {
		HasEditableValue<Client> getClientForm();
		HasEditableValue<InsurancePolicy> getInsurancePolicyForm();
		
		boolean isInsurancePolicyFormValid();
		
		public void setActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}
	
	protected boolean bound;
	protected Display view;
	protected EventBus eventBus;
	protected ClientProcessBroker clientBroker;
	protected Client client;
	
	public CreateInsurancePolicyViewPresenter(EventBus eventBus, Display view){
		setEventBus(eventBus);
		setView((View) view);
	}

	public void setClient(Client client){
		this.client = client;
		this.view.getClientForm().setValue(client);
		clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
	}
	
	@Override
	public void setService(Service service) {
		return;
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void setView(View view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();		
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void bind() {
		if(bound){return;}
		
		view.setActionHandler(new ActionInvokedEventHandler<CreateInsurancePolicyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case CREATE_POLICY:
					createPolicy();
					break;
				case CANCEL_POLICY_CREATION:
					onCreationCancelled();
					break;
				}
			}
		});
		
		bound = true;
	}

	protected void createPolicy(){
		if(view.isInsurancePolicyFormValid()){
			view.getInsurancePolicyForm().commit();
			this.clientBroker.createInsurancePolicy(this.client.processId, view.getInsurancePolicyForm().getValue(), new ResponseHandler<InsurancePolicy>() {
				
				@Override
				public void onResponse(InsurancePolicy response) {
					onPolicyCreated();
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
				}
			});
		}
	}
	
	public abstract void onCreationCancelled();
	public abstract void onPolicyCreated();
	
	@Override
	public void registerEventHandlers(EventBus eventBus) {
		return;
	}

	@Override
	public void setOperation(Operation o) {
		return;
	}

	@Override
	public Operation getOperation() {
		return null;
	}

	@Override
	public void goCompact(HasWidgets container) {
		// TODO Auto-generated method stub

	}

	@Override
	public String setTargetEntity(String id) {
		return null;
	}

	@Override
	public void setOperationPermission(boolean hasPermissionForOperation) {}

}
