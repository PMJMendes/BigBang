package bigBang.module.riskAnalisysModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.riskAnalisysModule.interfaces.RiskAnalisysServiceAsync;
import bigBang.module.riskAnalisysModule.shared.operation.RiskAnalisysSearchOperation;

public class RiskAnalisysSearchOperationViewPresenter implements
OperationViewPresenter {

	public interface Display {
		Widget asWidget();
	}

	protected RiskAnalisysServiceAsync service;
	protected EventBus eventBus;
	protected Display view;
	protected boolean bound;
	protected RiskAnalisysSearchOperation operation;

	public RiskAnalisysSearchOperationViewPresenter(EventBus evenBus, Service service, View view) {
		setEventBus(eventBus);
		setService(service);
		setView(view);
	}

	@Override
	public void setService(Service service) {
		this.service = (RiskAnalisysServiceAsync) service;
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
		this.operation = (RiskAnalisysSearchOperation) o;
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
		this.operation.setPermission(hasPermissionForOperation);
	}

}
