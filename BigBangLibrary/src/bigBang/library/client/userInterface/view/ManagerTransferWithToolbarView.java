package bigBang.library.client.userInterface.view;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ManagerTransferToolBar;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter.Action;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter.BarStatus;

public class ManagerTransferWithToolbarView extends ManagerTransferView implements ManagerTransferViewPresenter.Display{

	private ManagerTransferToolBar bar;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public ManagerTransferWithToolbarView(){
		super();
		this.bar = new ManagerTransferToolBar(false){

			@Override
			public void accept() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.ACCEPT));
			}

			@Override
			public void reject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.REJECT));
			}

			@Override
			public void cancel() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}
		};
		wrapper.insert(bar, 0);
		bar.setSize("100%", "100%");
	}

	@Override
	public void setToolBarState(BarStatus status) {
		if(status == BarStatus.ACCEPT_REJECT){
			bar.accept.setVisible(true);
			bar.reject.setVisible(true);
			bar.cancel.setVisible(false);
			bar.setVisible(true);
		}
		else if (status == BarStatus.CANCEL){
			bar.accept.setVisible(false);
			bar.reject.setVisible(false);
			bar.cancel.setVisible(true);
			bar.setVisible(true);
		}else {
			bar.accept.setVisible(false);
			bar.reject.setVisible(false);
			bar.cancel.setVisible(false);
			bar.setVisible(false);
		}
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}
}
