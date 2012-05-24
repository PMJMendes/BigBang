package bigBang.library.client.userInterface.view;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ManagerTransferToolBar;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter.Action;

public class ManagerTransferWithToolbarView extends ManagerTransferView implements ManagerTransferViewPresenter.Display{

	private ManagerTransferToolBar bar;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public ManagerTransferWithToolbarView(){
		super();
		this.bar = new ManagerTransferToolBar(){

			@Override
			public void onAccept() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.ACCEPT));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}
		};
		wrapper.insert(bar, 0);
		bar.setSize("100%", "100%");
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}
	
	
}
