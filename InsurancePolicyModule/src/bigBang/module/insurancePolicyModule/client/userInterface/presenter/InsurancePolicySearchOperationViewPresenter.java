package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.library.shared.Permission;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.shared.operation.InsurancePolicySearchOperation;

public class InsurancePolicySearchOperationViewPresenter implements
		OperationViewPresenter {

	public static enum Action {
		//TODO
	}
	
	public interface Display {
		//List
		HasValueSelectables<?> getList();

		//Form
		HasEditableValue<InsurancePolicy> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);

		//General
		void clear();
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewPolicy();
		void removeNewPolicyPreparation();
		void setSaveModeEnabled(boolean enabled);
		void setReadOnly(boolean readOnly);
		void clearAllowedPermissions();

		Widget asWidget();
		View getInstance();
	}
	
	protected EventBus eventBus;
	protected InsurancePolicyServiceAsync service;
	protected Display view;
	protected InsurancePolicySearchOperation operation;
	protected boolean bound = false;
	protected InsurancePolicyBroker broker;
	protected int dataVersion;
	
	public InsurancePolicySearchOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setService(service);
		setView(view);
		
		this.broker = ((InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY));
	}
	
	@Override
	public void setService(Service service) {
		this.service = (InsurancePolicyServiceAsync) service;
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
		if(this.bound)
			return;
		
		this.view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<InsurancePolicyStub> selected = (ValueSelectable<InsurancePolicyStub>) event.getFirstSelected();
				InsurancePolicyStub selectedValue = selected == null ? null : selected.getValue();
				view.setSaveModeEnabled(false);
				if(selectedValue == null){
					view.getForm().setValue(null);
					view.lockForm(true);
				}else{
					if(selectedValue.id != null){
						view.removeNewPolicyPreparation();
						BigBangPermissionManager.Util.getInstance().getProcessPermissions(selectedValue.processId, new ResponseHandler<Permission[]> () {

							@Override
							public void onResponse(Permission[] response) {
								view.clearAllowedPermissions();
								for(int i = 0; i < response.length; i++) {
									Permission p = response[i];
									if(p.instanceId == null){continue;}
									//TODO allowances
								}
							}

							@Override
							public void onError(Collection<ResponseError> errors) {}
							
						});
						broker.getPolicy(selectedValue.id, new ResponseHandler<InsurancePolicy>() {

							@Override
							public void onResponse(InsurancePolicy value) {
								view.getForm().setValue(value);
								view.lockForm(value == null);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {}
						});
					}
				}
			}
		});
	}

	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOperation(Operation o) {
		this.operation = (InsurancePolicySearchOperation)o;
	}

	@Override
	public Operation getOperation() {
		return this.operation;
	}

	@Override
	public void goCompact(HasWidgets container) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String setTargetEntity(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOperationPermission(boolean hasPermissionForOperation) {
		// TODO Auto-generated method stub
		
	}

}
