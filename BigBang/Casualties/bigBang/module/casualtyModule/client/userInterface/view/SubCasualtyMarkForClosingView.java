package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.casualtyModule.client.userInterface.SubCasualtyMarkForClosingForm;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyMarkForClosingToolbar;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyMarkForClosingViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyMarkForClosingViewPresenter.Action;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;

public class SubCasualtyMarkForClosingView extends View implements SubCasualtyMarkForClosingViewPresenter.Display {

	protected SubCasualtyMarkForClosingForm form;
	protected SubCasualtyMarkForClosingToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	
	public SubCasualtyMarkForClosingView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new SubCasualtyMarkForClosingToolbar(){
			
			@Override
			public void onMarkForClosing(){
				handler.onActionInvoked(new ActionInvokedEvent<SubCasualtyMarkForClosingViewPresenter.Action>(Action.MARK_FOR_CLOSING));
			}
			
			@Override
			public void onCancelRequest(){
				handler.onActionInvoked(new ActionInvokedEvent<SubCasualtyMarkForClosingViewPresenter.Action>(Action.CANCEL));
			}
		};
		wrapper.add(toolbar);
		
		form = new SubCasualtyMarkForClosingForm();
		wrapper.add(form.getNonScrollableContent());
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<String> getForm() {
		return form;
	}

	@Override
	public void allowMarkForClosing(boolean allow) {
		toolbar.allowMarkForClosing(allow);
	}

	@Override
	public void registerEventHandler(ActionInvokedEventHandler<Action> action) {
		this.handler = action;
	}

}
