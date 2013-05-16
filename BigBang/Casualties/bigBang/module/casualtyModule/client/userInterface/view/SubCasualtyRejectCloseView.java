package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyRejectCloseToolbar;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyRejectCloseForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyRejectCloseViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyRejectCloseViewPresenter.Action;

public class SubCasualtyRejectCloseView extends View implements SubCasualtyRejectCloseViewPresenter.Display {

	private ActionInvokedEventHandler<Action> actionHandler;
	private FormView<String> form;
	private SubCasualtyRejectCloseToolbar toolBar;

	public SubCasualtyRejectCloseView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		toolBar = new SubCasualtyRejectCloseToolbar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}

			@Override
			public void onReject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.REJECT_CLOSE));
			}
		};
		wrapper.add(toolBar);
		
		form = new SubCasualtyRejectCloseForm();
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
