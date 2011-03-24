package bigBang.module.generalSystemModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.client.userInterface.view.CoverageManagementOperationView;
import bigBang.module.generalSystemModule.shared.operation.CoverageManagementOperation;

public class CoverageManagementOperationViewPresenter implements
		OperationViewPresenter {
	
	public interface Display {
		Widget asWidget();
	}

	private EventBus eventBus;
	private Service service;
	private Display view;
	
	private CoverageManagementOperation operation;
	
	public CoverageManagementOperationViewPresenter(EventBus eventBus, Service service, Display view) {
		setView((View) view);
	}

	@Override
	public void setService(Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEventBus(EventBus eventBus) {
		// TODO Auto-generated method stub

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
		operation = (CoverageManagementOperation) o;
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

}
