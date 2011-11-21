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
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.library.shared.Permission;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateReceiptView;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.shared.operation.InsurancePolicySearchOperation;
import bigBang.module.insurancePolicyModule.shared.ModuleConstants;

public class InsurancePolicySearchOperationViewPresenter implements
		OperationViewPresenter {

	public static enum Action {
		EDIT,
		SAVE,
		CANCEL,
		DELETE,
		CREATE_RECEIPT,
		//TODO
	}
	
	public interface Display {
		//Listtype filter text
		HasValueSelectables<?> getList();

		//Form
		HasEditableValue<InsurancePolicy> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);

		//Create receipt
		void allowCreateReceipt(boolean allow);
		HasWidgets showCreateReceiptForm(boolean show);
		HasEditableValue<Receipt> getNewReceiptForm();
		boolean isNewReceiptFormValid();
		void lockNewReceiptForm(boolean lock);
		
		void allowUpdate(boolean allow);
		void allowDelete(boolean allow);
		
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
		
		view.clearAllowedPermissions();
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
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.EDIT_POLICY)){
										view.allowUpdate(true);
									}
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.DELETE_POLICY)){
										view.allowDelete(true);
									}
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.CREATE_RECEIPT)){
										view.allowCreateReceipt(true);
									}
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
		this.view.registerActionInvokedHandler(new ActionInvokedEventHandler<InsurancePolicySearchOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case SAVE:
					if(!view.isFormValid())
						return;
					InsurancePolicy info = view.getForm().getInfo();
					updatePolicy(info);
					break;
				case EDIT:
					broker.openPolicyResource(view.getForm().getValue(), new ResponseHandler<InsurancePolicy>() {

						@Override
						public void onResponse(InsurancePolicy response) {
							view.getForm().setValue(response);
							view.getForm().setReadOnly(false);
							view.setSaveModeEnabled(true);
						}
						@Override
						public void onError(Collection<ResponseError> errors) {}
					});
					break;
				case CANCEL:
					view.getForm().revert();
					view.getForm().setReadOnly(true);
					view.setSaveModeEnabled(false);
					break;
				case DELETE:
					deletePolicy(view.getForm().getValue().id);
					break;
				case CREATE_RECEIPT:
					createReceipt();
					break;
				}
			}
		});
	}
	
	public void updatePolicy(InsurancePolicy policy){
		this.broker.updatePolicy(policy, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
				view.setSaveModeEnabled(false);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
			}
		});
	}
	
	public void deletePolicy(String policyId){
		this.broker.removePolicy(policyId, new ResponseHandler<String>() {

			@Override
			public void onResponse(String response) {
				view.getForm().setValue(null);
				view.setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
			}
		});
	}
	
	protected void createReceipt(){
		HasWidgets container = view.showCreateReceiptForm(true);

		CreateReceiptView createReceiptView = new CreateReceiptView();
		CreateReceiptViewPresenter presenter = new CreateReceiptViewPresenter(null, createReceiptView) {

			@Override
			public void onReceiptCreated() {
				InsurancePolicySearchOperationViewPresenter.this.view.showCreateReceiptForm(false);
			}

			@Override
			public void onCreationCancelled() {
				InsurancePolicySearchOperationViewPresenter.this.view.showCreateReceiptForm(false);
			}

		};
		presenter.setPolicy(view.getForm().getValue());
		presenter.go(container);
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
