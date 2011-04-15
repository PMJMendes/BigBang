package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.interfaces.InsuranceAgencyServiceAsync;
import bigBang.module.generalSystemModule.shared.InsuranceAgency;
import bigBang.module.generalSystemModule.shared.operation.InsuranceAgencyManagementOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class InsuranceAgencyManagementOperationViewPresenter implements
		OperationViewPresenter {

	public interface Display {
		//List
		HasValueSelectables<InsuranceAgency> getList();
		void clearList();
		void addValuesToList(InsuranceAgency[] result);
		void removeInsuranceAgencyFromList(InsuranceAgency c);
		
		//Form
		HasEditableValue<InsuranceAgency> getForm();
		HasClickHandlers getSaveButton();
		HasClickHandlers getEditButton();
		HasClickHandlers getDeleteButton();
		boolean isFormValid();
		void lockForm(boolean lock);
		
		//General
		HasClickHandlers getNewButton();
		HasClickHandlers getRefreshButton();
		
		void prepareNewInsuranceAgency();
		void removeNewInsuranceAgencyPreparation();

		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}

	private InsuranceAgencyServiceAsync service;
	private Display view;
	private EventBus eventBus;
	
	private InsuranceAgencyManagementOperation operation;
	
	private boolean bound = false;
	
	private InsuranceAgency[] insuranceAgencies;

	public InsuranceAgencyManagementOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setService(service);
		setView(view);
	}

	public void setService(Service service) {
		this.service = (InsuranceAgencyServiceAsync)service;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setView(View view) {
		this.view = (Display)view;
	}

	public void go(HasWidgets container) {
		bind();
		bound = true;
		
		view.getList().setMultipleSelectability(false);
		view.getForm().setReadOnly(true);
		
		setup();
		
		container.clear();
		container.add(this.view.asWidget());
	}
	
	private void setup(){
		this.service.getInsuranceAgencies(new BigBangAsyncCallback<InsuranceAgency[]>() {
			public void onSuccess(InsuranceAgency[] result) {
				insuranceAgencies = result;
				setupGo();
			}
		});
	}
	
	private void setupGo(){
		if(this.insuranceAgencies == null)
			return;
		
		view.clearList();
		view.getForm().setValue(null);
		view.lockForm(true);
		view.addValuesToList(this.insuranceAgencies);
		
		this.insuranceAgencies = null;
	}

	public void bind() {
		if(bound)
			return;
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.size() == 0){
					view.getForm().setValue(null);
					return;
				}			
				
				for(Selectable s : selected) {
					@SuppressWarnings("unchecked")
					ValueSelectable<InsuranceAgency> vs = (ValueSelectable<InsuranceAgency>) s;
					InsuranceAgency value = vs.getValue();
					view.getForm().setValue(value);
					view.getForm().setReadOnly(true);
					view.lockForm(value == null);
					if(value.id != null){
						view.removeNewInsuranceAgencyPreparation();
					}
				}
			}
		});
		view.getNewButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.prepareNewInsuranceAgency();
				for(Selectable s : view.getList().getSelected()) {
					@SuppressWarnings("unchecked")
					ValueSelectable<InsuranceAgency> vs = (ValueSelectable<InsuranceAgency>) s;
					InsuranceAgency value = vs.getValue();
					view.getForm().setValue(value);
					view.getForm().setReadOnly(false);
					break;
				}
			}
		});
		view.getEditButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//TODO checkPermission
				view.getForm().setReadOnly(false);
			}
		});
		view.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!view.isFormValid())
					return;
				InsuranceAgency value = view.getForm().getValue();
				if(value.id == null)
					createInsuranceAgency(value);
				else
					saveInsuranceAgency(value);
			}
		});
		view.getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(view.getForm().getValue().id == null)
					view.removeNewInsuranceAgencyPreparation();
				else
					deleteInsuranceAgency(view.getForm().getValue());
			}
		});
		view.getRefreshButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setup();
			}
		});
	}
	public void createInsuranceAgency(InsuranceAgency c) {
		service.createInsuranceAgency(c, new BigBangAsyncCallback<InsuranceAgency>() {

			@Override
			public void onSuccess(InsuranceAgency result) {
				for(ValueSelectable<InsuranceAgency> s : view.getList().getSelected()){
					s.setValue(result);
					view.getForm().setValue(result);
					view.getForm().setReadOnly(true);
					break;
				}
			}
		});
	}
	public void saveInsuranceAgency(InsuranceAgency c) {
		service.saveInsuranceAgency(c, new BigBangAsyncCallback<InsuranceAgency>() {

			@Override
			public void onSuccess(InsuranceAgency result) {
				for(ValueSelectable<InsuranceAgency> s : view.getList().getSelected()){
					s.setValue(result);
					view.getForm().setValue(result);
					view.getForm().setReadOnly(true);
					break;
				}
			}
		});
	}
	
	public void deleteInsuranceAgency(final InsuranceAgency c) {
		//TODO alert
		service.deleteInsuranceAgency(c.id, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				view.removeInsuranceAgencyFromList(c);
				view.getList().clearSelection();
				view.getForm().setValue(null);
			}
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
