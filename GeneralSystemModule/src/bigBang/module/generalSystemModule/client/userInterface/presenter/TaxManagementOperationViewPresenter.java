package bigBang.module.generalSystemModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter.Display;
import bigBang.module.generalSystemModule.client.userInterface.view.TaxManagementOperationView;
import bigBang.module.generalSystemModule.interfaces.CoveragesServiceAsync;
import bigBang.module.generalSystemModule.interfaces.UserServiceAsync;
import bigBang.module.generalSystemModule.shared.operation.TaxManagementOperation;

public class TaxManagementOperationViewPresenter implements
		OperationViewPresenter {
	
	public interface Display {
		
		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}
	
	private CoveragesServiceAsync service;
	private Display view;
	private EventBus eventBus;
	
	private TaxManagementOperation operation;
	private boolean bound = false;

	public TaxManagementOperationViewPresenter(EventBus eventBus,
			Service coveragesService,
			TaxManagementOperationView view) {
		setEventBus(eventBus);
		setService(service);
		setView((View) view);
	}

	@Override
	public void setService(Service service) {
		this.service = (CoveragesServiceAsync) service;
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
		bound = true;
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void bind() {
		if(bound)
			return;
	}

	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOperation(Operation o) {
		this.operation = (TaxManagementOperation) o;
	}

	@Override
	public Operation getOperation() {
		return this.operation;
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
	public void setOperationPermission(boolean hasPermissionForOperation) {
		setReadOnly(!hasPermissionForOperation);
	}
	
	public void setReadOnly(boolean readOnly) {
		this.view.setReadOnly(readOnly);
	}

}
