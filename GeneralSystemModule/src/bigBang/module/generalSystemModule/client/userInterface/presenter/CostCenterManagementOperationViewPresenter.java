package bigBang.module.generalSystemModule.client.userInterface.presenter;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
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
		HasValue<CostCenter> getCostCenterList();

		void showDetailsForCostCenter(CostCenter costCenter);
		void setCostCenterEntries(CostCenter[] result);
		void updateCostCenterInfo(CostCenter result);
		HasClickHandlers getNewCostCenterButton();
		HasClickHandlers getSubmitNewCostCenterButton();
		HasClickHandlers getEditCostCenterButton();
		HasClickHandlers getSaveCostCenterButton();
		HasClickHandlers getDeleteCostCenterButton();
		
		void showNewCostCenterForm(boolean show);
		void setCostCenterFormEditable(boolean editable);
		boolean isCostCenterFormEditable();
		boolean isCostCenterFormValid();
		CostCenter getCostCenterInfo();
		
		boolean isNewCostCenterFormValid();
		CostCenter getNewCostCenterInfo();
		
		void removeCostCenter(String id);
		
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
		
		fetchCostCenterList();
		
		container.clear();
		container.add(this.view.asWidget());
	}

	private void fetchCostCenterList() {
		try{
			this.service.getCostCenterList(new BigBangAsyncCallback<CostCenter[]>() {
				public void onSuccess(CostCenter[] result) {
					view.setCostCenterEntries(result);
				}
			});
		}catch(Exception e){
			GWT.log("Error while fetching cost center list");
		}
	}

	public void bind() {
		this.view.getCostCenterList().addValueChangeHandler(new ValueChangeHandler<CostCenter>() {

			public void onValueChange(ValueChangeEvent<CostCenter> event) {
				view.showDetailsForCostCenter(event.getValue());
			}
		});
		this.view.getNewCostCenterButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.showNewCostCenterForm(true);
			}
		});
		this.view.getSubmitNewCostCenterButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(view.isNewCostCenterFormValid())
					createNewCostCenter(view.getNewCostCenterInfo());
			}
		});
		this.view.getEditCostCenterButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.setCostCenterFormEditable(!view.isCostCenterFormEditable());
			}
		});
		this.view.getSaveCostCenterButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(view.isCostCenterFormValid()){
					saveCostCenter(view.getCostCenterInfo());
				}
			}
		});
		this.view.getDeleteCostCenterButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deleteCostCenter(view.getCostCenterInfo());
			}
		});

	}
	
	public void createNewCostCenter(CostCenter c) {
		service.createCostCenter(c, new BigBangAsyncCallback<CostCenter>() {

			@Override
			public void onSuccess(CostCenter result) {
				view.showNewCostCenterForm(false);
				GWT.log("cost center created");
			}
		});
	}
	
	private void saveCostCenter(final CostCenter costCenter) {
		service.saveCostCenter(costCenter, new BigBangAsyncCallback<CostCenter>() {

			@Override
			public void onSuccess(CostCenter result) {
				view.setCostCenterFormEditable(false);
				view.updateCostCenterInfo(result);
			}
		});
	}

	private void deleteCostCenter(final CostCenter costCenter) {
		service.deleteCostCenter(costCenter.id, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				view.removeCostCenter(costCenter.id);
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
