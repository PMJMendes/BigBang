package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.clientModule.client.userInterface.CreateInsurancePolicyForm;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter.Action;
import bigBang.module.insurancePolicyModule.client.userInterface.CreateInsurancePolicyToolbar;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;

public class CreateInsurancePolicyView extends View implements CreateInsurancePolicyViewPresenter.Display {
	
	private ActionInvokedEventHandler<Action> handler;
	private CreateInsurancePolicyForm form;
	
	public CreateInsurancePolicyView() {
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		CreateInsurancePolicyToolbar toolbar = new CreateInsurancePolicyToolbar() {
			
			@Override
			public void onConfirm() {
				handler.onActionInvoked(new ActionInvokedEvent<CreateInsurancePolicyViewPresenter.Action>(Action.CONFIRM));
			}
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<CreateInsurancePolicyViewPresenter.Action>(Action.CANCEL));
			}
		};
		wrapper.add(toolbar);
		
		form = new CreateInsurancePolicyForm();
		wrapper.add(form.getNonScrollableContent());
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public String getCategory() {
		return form.getCategory();
	}

	@Override
	public String getLine() {
		return form.getLine();
	}

	@Override
	public String getSubLine() {
		return form.getSubLine();
	}

	@Override
	public void clear() {
		form.setValue(null);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

}
