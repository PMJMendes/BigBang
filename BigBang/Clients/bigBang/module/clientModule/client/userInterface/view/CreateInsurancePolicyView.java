package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.clientModule.client.userInterface.form.CreateInsurancePolicyForm;
import bigBang.module.clientModule.client.userInterface.form.CreateInsurancePolicyFormValidator;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter.Action;
import bigBang.module.insurancePolicyModule.client.userInterface.CreateInsurancePolicyToolbar;
import bigBang.library.client.HasEditableValue;
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
		form.setValidator(new CreateInsurancePolicyFormValidator(form));
		wrapper.add(form.getNonScrollableContent());
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void clear() {
		form.setValue(null);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public HasEditableValue<String> getForm() {
		return form;
	}

}
