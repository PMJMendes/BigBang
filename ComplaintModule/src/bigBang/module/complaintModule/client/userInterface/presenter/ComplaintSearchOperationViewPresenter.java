package bigBang.module.complaintModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.complaintModule.interfaces.ComplaintServiceAsync;
import bigBang.module.complaintModule.shared.operation.ComplaintSearchOperation;

public class ComplaintSearchOperationViewPresenter implements
		OperationViewPresenter {

	public interface Display {
		Widget asWidget();
	}

	protected boolean bound = false;
	protected Display view;
	protected EventBus eventBus;
	protected ComplaintServiceAsync service;
	protected ComplaintSearchOperation operation;
	
	public ComplaintSearchOperationViewPresenter(EventBus eventBus, Service service, View view){
		this.setService(service);
		this.setView(view);
		this.setEventBus(eventBus);
	}

	public void setService(Service service) {
		this.service = (ComplaintServiceAsync) service;
	}

	public void setEventBus(final EventBus eventBus) {
		this.eventBus = eventBus;
		if(this.eventBus == null)
			return;
		registerEventHandlers(eventBus);
	}

	public void setView(View view) {
		this.view = (Display) view;
	}

	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
	}
	
	//The compact version of the operation view
	public void goCompact(HasWidgets container){
		go(container);
		/*this.bind();
		container.clear();
		container.add((Widget)this.view.getSearchPreviewPanelContainer());*/
	}

	public void bind() {
		if(bound)
			return;
	}

	public void setOperation(final ComplaintSearchOperation operation) {
		this.operation = operation;
	}

	public void registerEventHandlers(final EventBus eventBus) {
		//TODO
	}

	public void setOperation(Operation o) {
		this.operation = (ComplaintSearchOperation)o;
	}

	public Operation getOperation() {
		return this.operation;
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

	protected void setReadOnly(boolean result) {
		// TODO Auto-generated method stub
		
	}
}
