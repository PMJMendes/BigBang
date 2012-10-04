package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyDeleteToolBar;
import bigBang.module.insurancePolicyModule.client.userInterface.form.SubPolicyDeleteForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyDeleteViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyDeleteViewPresenter.Action;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;

public class SubPolicyDeleteView extends View implements SubPolicyDeleteViewPresenter.Display {

	private ActionInvokedEventHandler<Action> actionHandler;
	private FormView<String> form;
	private SubPolicyDeleteToolBar toolBar;

	public SubPolicyDeleteView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		toolBar = new SubPolicyDeleteToolBar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyDeleteViewPresenter.Action>(Action.CANCEL));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyDeleteViewPresenter.Action>(Action.DELETE));
			}
		};
		wrapper.add(toolBar);
		
		form = new SubPolicyDeleteForm();
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");

		initWidget(wrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<String> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

}
