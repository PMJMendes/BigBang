package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.casualtyModule.client.userInterface.CasualtyCloseToolBar;
import bigBang.module.casualtyModule.client.userInterface.form.CasualtyCloseForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyCloseViewPresenter;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;

public class CasualtyCloseView extends View implements CasualtyCloseViewPresenter.Display {

	private ActionInvokedEventHandler<CasualtyCloseViewPresenter.Action> actionHandler;
	private FormView<String> form;
	private CasualtyCloseToolBar toolBar;

	public CasualtyCloseView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		toolBar = new CasualtyCloseToolBar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtyCloseViewPresenter.Action>(CasualtyCloseViewPresenter.Action.CANCEL));
			}

			@Override
			public void onClose() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtyCloseViewPresenter.Action>(CasualtyCloseViewPresenter.Action.CLOSE));
			}
		};
		wrapper.add(toolBar);
		
		form = new CasualtyCloseForm();
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
	public void registerActionHandler(ActionInvokedEventHandler<CasualtyCloseViewPresenter.Action> handler) {
		this.actionHandler = handler;
	}

}
