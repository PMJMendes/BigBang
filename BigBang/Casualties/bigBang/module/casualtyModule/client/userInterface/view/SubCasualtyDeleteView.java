package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyDeleteViewPresenter;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.casualtyModule.client.userInterface.SubCasualtyDeleteForm;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyDeleteToolBar;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;

public class SubCasualtyDeleteView extends View implements SubCasualtyDeleteViewPresenter.Display {

	private ActionInvokedEventHandler<SubCasualtyDeleteViewPresenter.Action> actionHandler;
	private FormView<String> form;
	private SubCasualtyDeleteToolBar toolBar;

	public SubCasualtyDeleteView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		toolBar = new SubCasualtyDeleteToolBar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyDeleteViewPresenter.Action>(SubCasualtyDeleteViewPresenter.Action.CANCEL));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyDeleteViewPresenter.Action>(SubCasualtyDeleteViewPresenter.Action.DELETE));
			}
		};
		wrapper.add(toolBar);
		
		form = new SubCasualtyDeleteForm();
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
	public void registerActionHandler(ActionInvokedEventHandler<SubCasualtyDeleteViewPresenter.Action> handler) {
		this.actionHandler = handler;
	}

}
