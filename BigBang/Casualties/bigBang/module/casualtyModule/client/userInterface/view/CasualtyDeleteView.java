package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.casualtyModule.client.userInterface.CasualtyDeleteToolBar;
import bigBang.module.casualtyModule.client.userInterface.form.CasualtyDeleteForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyDeleteViewPresenter;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;

public class CasualtyDeleteView extends View implements CasualtyDeleteViewPresenter.Display {

	private ActionInvokedEventHandler<CasualtyDeleteViewPresenter.Action> actionHandler;
	private FormView<String> form;
	private CasualtyDeleteToolBar toolBar;

	public CasualtyDeleteView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		toolBar = new CasualtyDeleteToolBar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtyDeleteViewPresenter.Action>(CasualtyDeleteViewPresenter.Action.CANCEL));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtyDeleteViewPresenter.Action>(CasualtyDeleteViewPresenter.Action.DELETE));
			}
		};
		wrapper.add(toolBar);
		
		form = new CasualtyDeleteForm();
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
	public void registerActionHandler(ActionInvokedEventHandler<CasualtyDeleteViewPresenter.Action> handler) {
		this.actionHandler = handler;
	}

}
