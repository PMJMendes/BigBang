package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.CasualtyReopenToolbar;
import bigBang.module.casualtyModule.client.userInterface.form.ReopenCasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.ReopenCasualtyViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.ReopenCasualtyViewPresenter.Action;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ReopenCasualtyView extends View implements ReopenCasualtyViewPresenter.Display{

	private ActionInvokedEventHandler<Action> actionHandler;
	private ReopenCasualtyForm form;
	private CasualtyReopenToolbar toolbar;

	public ReopenCasualtyView() {
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		toolbar = new CasualtyReopenToolbar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReopenCasualtyViewPresenter.Action>(Action.CANCEL));
			}

			@Override
			public void onReopen() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReopenCasualtyViewPresenter.Action>(Action.REOPEN));
			}
		};

		wrapper.add(toolbar);

		form = new ReopenCasualtyForm();
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
