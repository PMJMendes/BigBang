package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsuranceAgencyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.client.ContactManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.operation.InsuranceAgencyManagementOperation;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class InsuranceAgencyManagementOperationViewPresenter implements
		OperationViewPresenter {

	public static enum Action{
		NEW,
		REFRESH,
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE
	}

	public interface Display {
		//List
		HasValueSelectables<InsuranceAgency> getList();

		//Form
		HasEditableValue<InsuranceAgency> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);

		//General
		void clear();
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewInsuranceAgency();
		void removeNewInsuranceAgencyPreparation();
		void setSaveModeEnabled(boolean enabled);

		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}

	private Display view;
	@SuppressWarnings("unused")
	private EventBus eventBus;
	
	private InsuranceAgencyManagementOperation operation;
	
	private boolean bound = false;
	private ContactManager contactManager; //TODO
	protected InsuranceAgencyBroker insuranceAgencyBroker;

	public InsuranceAgencyManagementOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setService(service);
		setView(view);
		this.contactManager = new ContactManager();
		this.insuranceAgencyBroker = (InsuranceAgencyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_AGENCY);
	}

	public void setService(Service service) {}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setView(View view) {
		this.view = (Display)view;
	}

	public void go(HasWidgets container) {
		bind();
		bound = true;
		view.clear();

		view.getList().setMultipleSelectability(false);
		view.getForm().setReadOnly(true);
		fetchInsuranceAgencies();

		container.clear();
		container.add(this.view.asWidget());
	}
	
	private void fetchInsuranceAgencies(){
		this.insuranceAgencyBroker.getInsuranceAgencies(new ResponseHandler<InsuranceAgency[]>() {
			
			@Override
			public void onResponse(InsuranceAgency[] response) {}
			
			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}
	
	public void bind() {
		if(bound)
			return;
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<InsuranceAgency> selected = (ValueSelectable<InsuranceAgency>) event.getFirstSelected();
				InsuranceAgency selectedValue = selected == null ? null : selected.getValue();
				if(selectedValue == null){
					view.getForm().setValue(null);
					view.lockForm(true);
				}else{
					if(selectedValue.id != null){
						view.removeNewInsuranceAgencyPreparation();
						insuranceAgencyBroker.getInsuranceAgency(selectedValue.id, new ResponseHandler<InsuranceAgency>() {

							@Override
							public void onResponse(InsuranceAgency value) {
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
		view.registerActionInvokedHandler(new ActionInvokedEventHandler<InsuranceAgencyManagementOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					view.prepareNewInsuranceAgency();
					for(Selectable s : view.getList().getSelected()) {
						@SuppressWarnings("unchecked")
						ValueSelectable<InsuranceAgency> vs = (ValueSelectable<InsuranceAgency>) s;
						InsuranceAgency value = vs.getValue();
						view.getForm().setValue(value);
						view.getForm().setReadOnly(false);
						break;
					}
					break;
				case REFRESH:
					fetchInsuranceAgencies();
					break;
				case DELETE:
					if(view.getForm().getValue().id == null)
						view.removeNewInsuranceAgencyPreparation();
					else
						deleteInsuranceAgency(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					if(!view.isFormValid())
						return;
					InsuranceAgency info = view.getForm().getInfo();
					if(info.id == null)
						createInsuranceAgency(info);
					else
						saveInsuranceAgency(info);
					break;
				case CANCEL_EDIT:
					if(view.getForm().getInfo().id == null){
						view.removeNewInsuranceAgencyPreparation();
					}else{
						view.getForm().revert();
						view.getForm().setReadOnly(true);
					}
					break;
				default:
					break;
				}
			}
		});
	}

	public void createInsuranceAgency(InsuranceAgency c) {
		this.insuranceAgencyBroker.addInsuranceAgency(c, new ResponseHandler<InsuranceAgency>() {

			@Override
			public void onResponse(InsuranceAgency response) {
				for(ValueSelectable<InsuranceAgency> s : view.getList().getSelected()){
					s.setValue(response);
					view.getForm().setValue(response);
					view.getForm().setReadOnly(true);
					break;
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	public void saveInsuranceAgency(InsuranceAgency c) {
		
		this.insuranceAgencyBroker.updateInsuranceAgency(c, new ResponseHandler<InsuranceAgency>() {

			@Override
			public void onResponse(InsuranceAgency response) {
				for(ValueSelectable<InsuranceAgency> s : view.getList().getSelected()){
					s.setValue(response);
					view.getForm().setValue(response);
					view.getForm().setReadOnly(true);
					break;
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}
	
	public void deleteInsuranceAgency(InsuranceAgency c) {
		this.insuranceAgencyBroker.removeInsuranceAgency(c.id, new ResponseHandler<InsuranceAgency>() {

			@Override
			public void onResponse(InsuranceAgency response) {
				view.getList().clearSelection();
				view.getForm().setValue(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}	

	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	public void setOperation(Operation o) {
		this.operation = (InsuranceAgencyManagementOperation) o;
	}

	public Operation getOperation() {
		return operation;
	}

	public void goCompact(HasWidgets container) {
		// TODO Auto-generated method stub

	}

	public String setTargetEntity(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOperationPermission(boolean result) {
		this.operation.setPermission(result);
		setReadOnly(result);
	}

	private void setReadOnly(boolean result) {
		view.setReadOnly(result);
	}

}
