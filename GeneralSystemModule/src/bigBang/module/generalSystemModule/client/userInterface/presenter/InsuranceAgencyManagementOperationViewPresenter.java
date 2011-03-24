package bigBang.module.generalSystemModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.operation.InsuranceAgencyManagementOperation;

public class InsuranceAgencyManagementOperationViewPresenter implements
		OperationViewPresenter {

	private InsuranceAgencyManagementOperation operation;
	
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
		// TODO Auto-generated method stub

	}

	@Override
	public void go(HasWidgets container) {
		// TODO Auto-generated method stub

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

}
