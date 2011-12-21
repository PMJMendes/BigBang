package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObjectStub;
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
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.client.userInterface.view.UndoOperationView;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.library.shared.Permission;
import bigBang.module.insurancePolicyModule.client.userInterface.view.CreateReceiptView;
import bigBang.module.insurancePolicyModule.client.userInterface.view.InsuredObjectView;
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
		CREATE_INSURED_OBJECT
		//TODO
	}
	
	public interface Display {
		//Listtype filter text
		HasValueSelectables<?> getList();

		//Form
		HasEditableValue<InsurancePolicy> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);
		
		String getInsuredObjectTableFilter();
		String getExerciseTableFilter();
		TableSection getCurrentTablePage();

		//children lists
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<InsuredObjectStub> getObjectsList();
		HasValueSelectables<HistoryItemStub> getHistoryList();
		
		void showHistory(InsurancePolicy policy, String selectedItemId);
		
		//Create receipt
		void allowCreateReceipt(boolean allow);
		HasWidgets showCreateReceiptForm(boolean show);
		HasEditableValue<Receipt> getNewReceiptForm();
		boolean isNewReceiptFormValid();
		void lockNewReceiptForm(boolean lock);
		
		//Create Insured Object
		HasWidgets showCreateInsuredObjectView(boolean show);
		void allowCreateInsuredObject(boolean allow);
		
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
	protected InsuredObjectDataBroker insuredObjectBroker;
	protected int dataVersion;
	
	protected InsurancePolicyDataBrokerClient insurancePolicyClient;
	protected InsuredObjectDataBrokerClient insuredObjectClient;
	
	public InsurancePolicySearchOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setService(service);
		setView(view);
		
		this.broker = ((InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY));
		this.insuredObjectBroker = ((InsuredObjectDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT));
		
		this.insurancePolicyClient = initInsurancePolicyClient();
		this.insuredObjectClient = initInsuredObjectClient();
		
		this.broker.registerClient(this.insurancePolicyClient);
		this.insuredObjectBroker.registerClient(insuredObjectClient);
	}
	
	protected InsurancePolicyDataBrokerClient initInsurancePolicyClient(){
		InsurancePolicyDataBrokerClient result = new InsurancePolicyDataBrokerClient() {
			
			protected int version;
			
			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				this.version = number;
			}
			
			@Override
			public int getDataVersion(String dataElementId) {
				return this.version;
			}
			
			@Override
			public void updateInsurancePolicy(InsurancePolicy policy) {
				//TODO
			}
			
			@Override
			public void removeInsurancePolicy(String policyId) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void addInsurancePolicy(InsurancePolicy policy) {
				// TODO Auto-generated method stub
				
			}
		};
		return result;
	}
	
	protected InsuredObjectDataBrokerClient initInsuredObjectClient() {
		InsuredObjectDataBrokerClient result = new InsuredObjectDataBrokerClient() {

			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public int getDataVersion(String dataElementId) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void updateInsuredObject(InsuredObject object) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void removeInsuredObject(String id) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addInsuredObject(InsuredObject object) {
				// TODO Auto-generated method stub
				
			}
		};
		return result;
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
				view.clearAllowedPermissions();
				view.lockForm(true);
				@SuppressWarnings("unchecked")
				ValueSelectable<InsurancePolicyStub> selected = (ValueSelectable<InsurancePolicyStub>) event.getFirstSelected();
				final InsurancePolicyStub selectedValue = selected == null ? null : selected.getValue();
				view.setSaveModeEnabled(false);
				if(selectedValue == null){
					view.getForm().setValue(null);
				}else{
					if(selectedValue.id != null){
						view.removeNewPolicyPreparation();
						view.clearAllowedPermissions();
						broker.getPolicy(selectedValue.id, new ResponseHandler<InsurancePolicy>() {

							@Override
							public void onResponse(InsurancePolicy value) {
								view.getForm().setValue(value);
								updatePermissions();
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
					broker.openPolicyResource(view.getForm().getInfo(), new ResponseHandler<InsurancePolicy>() {

						@Override
						public void onResponse(InsurancePolicy response) {
							view.getForm().setInfo(response);
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
					deletePolicy(view.getForm().getInfo().id);
					break;
				case CREATE_RECEIPT:
					createReceipt();
					break;
				}
			}
		});
		view.getContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		view.getDocumentsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		view.getObjectsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				InsuredObjectStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<InsuredObjectStub>)event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showInsuredObject(selectedValue);
				}
			}
		});
		view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				HistoryItemStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<HistoryItemStub>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showHistory(selectedValue);
				}
			}
		});
	}
	
	protected void updatePermissions(){
		view.clearAllowedPermissions();
		InsurancePolicy policy = view.getForm().getValue();
		
		if(broker.isTemp(policy)){
			view.allowUpdate(true);
			view.allowDelete(true);
			view.allowCreateInsuredObject(true);
		}else{
			BigBangPermissionManager.Util.getInstance().getProcessPermissions(policy.processId, new ResponseHandler<Permission[]> () {
				@Override
				public void onResponse(final Permission[] response) {
					view.lockForm(true);
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
						if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.CREATE_INSURED_OBJECT)){
							view.allowCreateInsuredObject(true);
						}
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {}
						
			});
		}
	}
	
	public void updatePolicy(final InsurancePolicy policy){
		this.broker.updatePolicy(policy, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(final InsurancePolicy policyResponse) {
				
				broker.saveCoverageDetailsPage(policy.id, view.getInsuredObjectTableFilter(), view.getExerciseTableFilter(), view.getCurrentTablePage(), new ResponseHandler<TableSection>() {

					@Override
					public void onResponse(TableSection response) {
						broker.commitPolicy(policyResponse, new ResponseHandler<InsurancePolicy>() {

							@Override
							public void onResponse(InsurancePolicy response) {
								view.getForm().setValue(response);
								view.getForm().setReadOnly(true);
								view.setSaveModeEnabled(false);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								//TODO
							}
						});
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						// TODO Auto-generated method stub
					}
				});
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

	protected void showInsuredObject(InsuredObjectStub object) {
		HasWidgets container = view.showCreateInsuredObjectView(true);
		final InsuredObjectViewPresenter presenter = new InsuredObjectViewPresenter(eventBus, new InsuredObjectView()){

			@Override
			public void onSave() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDelete() {
				// TODO Auto-generated method stub
				
			}
			
		};
		presenter.setPolicy(view.getForm().getValue());
		insuredObjectBroker.getInsuredObject(object.id, new ResponseHandler<InsuredObject>() {
			
			@Override
			public void onResponse(InsuredObject response) {
				presenter.setInsuredObject(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
		presenter.go(container);
	}
	
	protected void showHistory(HistoryItemStub historyItem) {
		view.showHistory(view.getForm().getValue(), historyItem.id);
	}
	
	@Override
	public void registerEventHandlers(EventBus eventBus) {
		return;
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
		return;
	}
		

	@Override
	public String setTargetEntity(String id) {
		return null;
	}

	@Override
	public void setOperationPermission(boolean hasPermissionForOperation) {
		return;
	}

}
