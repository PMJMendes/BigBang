package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.form.DeleteClientForm;
import bigBang.module.clientModule.client.userInterface.form.DeleteClientFormValidator;
import bigBang.module.clientModule.client.userInterface.DeleteClientToolbar;
import bigBang.module.clientModule.client.userInterface.presenter.DeleteClientViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.DeleteClientViewPresenter.Action;

public class DeleteClientView extends View implements DeleteClientViewPresenter.Display{

	private ActionInvokedEventHandler<Action> actionHandler;
	private DeleteClientForm form;
	private DeleteClientToolbar toolBar;

	public DeleteClientView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		toolBar = new DeleteClientToolbar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<DeleteClientViewPresenter.Action>(Action.CANCEL));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<DeleteClientViewPresenter.Action>(Action.DELETE));
			}
		};
		
		wrapper.add(toolBar);
		form = new DeleteClientForm();
		form.setValidator(new DeleteClientFormValidator(form));
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellWidth(form.getNonScrollableContent(), "100%");
	}


	@Override
	public HasEditableValue<String> getForm() {
		return form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}
	@Override
	protected void initializeView() {
		return;
	}
}
