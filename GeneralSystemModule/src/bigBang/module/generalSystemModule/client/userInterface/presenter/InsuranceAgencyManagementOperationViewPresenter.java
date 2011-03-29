package bigBang.module.generalSystemModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.operation.InsuranceAgencyManagementOperation;

public class InsuranceAgencyManagementOperationViewPresenter implements
		OperationViewPresenter {

	public interface Display {
		Widget asWidget();
	}
	
	private EventBus eventBus;
	private Display view;
	private Service service;
	
	private InsuranceAgencyManagementOperation operation;
	
	public InsuranceAgencyManagementOperationViewPresenter(EventBus eventBus, Service service, View view) {
		setView(view);
		setEventBus(eventBus);
		setService(service);
	}
	
	@Override
	public void setService(Service service) {
		
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
		container.add(view.asWidget());
	}

	@Override
	public void bind() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOperation(Operation o) {
		operation = (InsuranceAgencyManagementOperation) o;
	}

	@Override
	public Operation getOperation() {
		return operation;
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
	public void setOperationPermission(boolean result) {
		this.operation.setPermission(result);
		setReadOnly(result);
	}

	private void setReadOnly(boolean result) {
		// TODO Auto-generated method stub
		
	}
	
	
	//Methods that require services
	

}
