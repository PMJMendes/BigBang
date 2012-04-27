package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.casualtyModule.client.userInterface.CasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.CasualtyTasksOperationsToolbar;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyTasksViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyTasksViewPresenter.Action;
import bigBang.definitions.shared.Casualty;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;

public class CasualtyTasksView extends View implements CasualtyTasksViewPresenter.Display{

	protected CasualtyForm form;
	protected CasualtyTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	
	public CasualtyTasksView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		toolbar = new CasualtyTasksOperationsToolbar() {
			
			@Override
			public void onClose() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CLOSE));
			}
		};
		wrapper.add(toolbar);
		
		form = new CasualtyForm();
		form.setReadOnly(true);
		form.setSize("100%", "100%");
		wrapper.add(form);
		wrapper.setCellHeight(form, "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<Casualty> getForm() {
		return form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		toolbar.hideAll();
	}

	@Override
	public void allowClose(boolean allow) {
		toolbar.allowClose(allow);
	}

}
