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
import bigBang.module.generalSystemModule.interfaces.CostCenterServiceAsync;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.operation.CostCenterManagementOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class CostCenterManagementOperationViewPresenter implements
OperationViewPresenter {

	public interface Display {
		//List
		HasValueSelectables<CostCenter> getList();
		void clearList();
		void addValuesToList(CostCenter[] result);
		void removeCostCenterFromList(CostCenter c);
		
		//Form
		HasEditableValue<CostCenter> getForm();
		HasClickHandlers getSaveButton();
		HasClickHandlers getEditButton();
		HasClickHandlers getDeleteButton();
		boolean isFormValid();
		void lockForm(boolean lock);
		
		//Members list
		HasValueSelectables<User> getMembersList();
		void clearMembersList();
		void addValuesToMembersList(User[] result);
		
		//General
		HasClickHandlers getNewButton();
		HasClickHandlers getRefreshButton();
		
		void prepareNewCostCenter();
		void removeNewCostCenterPreparation();

		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}

	private CostCenterServiceAsync service;
	private Display view;
	private EventBus eventBus;
	
	private CostCenterManagementOperation operation;
	
	private boolean bound = false;

	public CostCenterManagementOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setService(service);
		setView(view);
	}

	public void setService(Service service) {
		this.service = (CostCenterServiceAsync)service;
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
		fetchCostCenterList();
		
		container.clear();
		container.add(this.view.asWidget());
	}

	private void fetchCostCenterList() {
		try{
			this.service.getCostCenterList(new BigBangAsyncCallback<CostCenter[]>() {
				public void onSuccess(CostCenter[] result) {
					view.clearList();
					view.getForm().setValue(null);
					view.lockForm(true);
					view.addValuesToList(result);
				}
			});
		}catch(Exception e){
			GWT.log("Error while fetching cost center list");
		}
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
					ValueSelectable<CostCenter> vs = (ValueSelectable<CostCenter>) s;
					CostCenter value = vs.getValue();
					view.getForm().setValue(value);
					view.getForm().setReadOnly(true);
					view.clearMembersList();
					view.addValuesToMembersList(value.members);
					view.lockForm(value == null);
					if(value.id != null){
						view.removeNewCostCenterPreparation();
					}
				}
			}
		});
		view.getNewButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.prepareNewCostCenter();
				for(Selectable s : view.getList().getSelected()) {
					@SuppressWarnings("unchecked")
					ValueSelectable<CostCenter> vs = (ValueSelectable<CostCenter>) s;
					CostCenter value = vs.getValue();
					view.getForm().setValue(value);
					view.getForm().setReadOnly(false);
					view.clearMembersList();
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
				CostCenter value = view.getForm().getValue();
				if(value.id == null)
					createCostCenter(value);
				else
					saveCostCenter(value);
			}
		});
		view.getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(view.getForm().getValue().id == null)
					view.removeNewCostCenterPreparation();
				else
					deleteCostCenter(view.getForm().getValue());
			}
		});
		view.getRefreshButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fetchCostCenterList();
			}
		});
	}
	public void createCostCenter(CostCenter c) {
		service.createCostCenter(c, new BigBangAsyncCallback<CostCenter>() {

			@Override
			public void onSuccess(CostCenter result) {
				for(ValueSelectable<CostCenter> s : view.getList().getSelected()){
					s.setValue(result);
					view.getForm().setValue(result);
					view.getForm().setReadOnly(true);
					break;
				}
			}
		});
	}
	public void saveCostCenter(CostCenter c) {
		service.saveCostCenter(c, new BigBangAsyncCallback<CostCenter>() {

			@Override
			public void onSuccess(CostCenter result) {
				for(ValueSelectable<CostCenter> s : view.getList().getSelected()){
					s.setValue(result);
					view.getForm().setValue(result);
					view.getForm().setReadOnly(true);
					break;
				}
			}
		});
	}
	
	public void deleteCostCenter(final CostCenter c) {
		//TODO alert
		service.deleteCostCenter(c.id, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				view.removeCostCenterFromList(c);
				view.getList().clearSelection();
				view.clearMembersList();
				view.getForm().setValue(null);
			}
		});
	}	

	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	public void setOperation(Operation o) {
		this.operation = (CostCenterManagementOperation) o;
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
	public void setOperationPermission(boolean permission) {
		this.operation.setPermission(permission);
		this.setReadOnly(permission);
	}

	private void setReadOnly(boolean result) {
		view.setReadOnly(result);
	}

}
