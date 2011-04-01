package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.List;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
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
import bigBang.module.generalSystemModule.shared.operation.CostCenterManagementOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class CostCenterManagementOperationViewPresenter implements
OperationViewPresenter {

	public interface Display {
		//List
		HasValueSelectables<CostCenter> getList();
		void addValuesToList(CostCenter[] result);
		void removeCostCenterFromList(CostCenter c);
		
		//Form
		HasValue<CostCenter> getForm();
		HasClickHandlers getDeleteButton();
		
		//General
		HasClickHandlers getNewButton();
		HasClickHandlers getRefreshButton();
		
		void prepareNewCostCenter();
		
		
		Widget asWidget();

		

	}

	private CostCenterServiceAsync service;
	private Display view;
	private EventBus eventBus;
	
	private CostCenterManagementOperation operation;

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

		view.getList().setMultipleSelectability(false);
		fetchCostCenterList();
		
		container.clear();
		container.add(this.view.asWidget());
	}

	private void fetchCostCenterList() {
		try{
			this.service.getCostCenterList(new BigBangAsyncCallback<CostCenter[]>() {
				public void onSuccess(CostCenter[] result) {
					view.addValuesToList(result);
				}
			});
		}catch(Exception e){
			GWT.log("Error while fetching cost center list");
		}
	}

	public void bind() {
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				for(Selectable s : event.getSelected()) {
					@SuppressWarnings("unchecked")
					ValueSelectable<CostCenter> vs = (ValueSelectable<CostCenter>) s;
					CostCenter value = vs.getValue();
					view.getForm().setValue(value);
				}
			}
		});
		view.getNewButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.prepareNewCostCenter();
			}
		});
		/*view.getForm().addValueChangeHandler(new ValueChangeHandler<CostCenter>() {
=======

	}
	
	public void createNewCostCenter(CostCenter c) {
		service.createCostCenter(c, new BigBangAsyncCallback<CostCenter>() {
>>>>>>> .r142

			@Override
<<<<<<< .mine
			public void onValueChange(ValueChangeEvent<CostCenter> event) {
				CostCenter c = event.getValue();
				if(c.id == null)
					createCostCenter(c);
				else
					saveCostCenter(c);
=======
			public void onSuccess(CostCenter result) {
				view.showNewCostCenterForm(false);
				GWT.log("cost center created");
>>>>>>> .r142
			}
		});*/
	}
	
	public void createCostCenter(CostCenter c) {
		
	}

	public void saveCostCenter(CostCenter c) {
		service.saveCostCenter(c, new BigBangAsyncCallback<CostCenter>() {

			@Override
			public void onSuccess(CostCenter result) {
				
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

	private void setReadOnly(boolean permission) {
		// TODO Auto-generated method stub
		
	}

}
