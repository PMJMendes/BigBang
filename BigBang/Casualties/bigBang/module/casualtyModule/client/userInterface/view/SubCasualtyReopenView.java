package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyReopenToolbar;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyReopenForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyReopenViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyReopenViewPresenter.Action;

import com.google.gwt.user.client.ui.VerticalPanel;

public class SubCasualtyReopenView extends View implements SubCasualtyReopenViewPresenter.Display{
	
	private ActionInvokedEventHandler<Action> actionHandler;
	private SubCasualtyReopenForm form;
	private SubCasualtyReopenToolbar toolbar;
	
	public SubCasualtyReopenView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		toolbar = new SubCasualtyReopenToolbar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}

			@Override
			public void onReopen() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.REOPEN));
			}
		};

		wrapper.add(toolbar);

		form = new SubCasualtyReopenForm();
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");

		initWidget(wrapper);
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<String[]> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void addSubCasualtyItem(TipifiedListItem item) {
		form.addItem(item);
	}

	@Override
	public void clearSubCasualties() {
		form.clearItems();
	}


}
