package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.InfoOrDocumentRequestOperationsToolbar;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestViewPresenter;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestViewPresenter.Action;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class InfoOrDocumentRequestView extends View implements InfoOrDocumentRequestViewPresenter.Display{
	
	protected InfoOrDocumentRequestForm form;
	protected InfoOrDocumentRequestOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected HasWidgets ownerFormContainer; 
	
	public InfoOrDocumentRequestView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		this.toolbar = new InfoOrDocumentRequestOperationsToolbar() {
			
			@Override
			public void onSendRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InfoOrDocumentRequestViewPresenter.Action>(Action.SEND));
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InfoOrDocumentRequestViewPresenter.Action>(Action.CANCEL));
			}
		};

		SimplePanel ownerFormContainer = new SimplePanel();
		ownerFormContainer.setSize("100%", "100%");
		this.ownerFormContainer = ownerFormContainer;
		mainWrapper.addWest(ownerFormContainer, 665);
		
		VerticalPanel requestFormWrapper = new VerticalPanel();
		requestFormWrapper.setSize("100%", "100%");
		ListHeader requestHeader = new ListHeader("Pedido");
		requestFormWrapper.add(requestHeader);
		requestHeader.setHeight("30px");
		requestFormWrapper.add(toolbar);
		requestFormWrapper.setCellHeight(toolbar, "21px");
		form = new InfoOrDocumentRequestForm();
		requestFormWrapper.add(form);
		requestFormWrapper.setCellHeight(form, "100%");
		mainWrapper.add(requestFormWrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
	}
	
	@Override
	public HasEditableValue<InfoOrDocumentRequest> getForm() {
		return this.form;
	}

	@Override
	public void clearAllowedPermissions() {
		this.toolbar.lockAll();
	}

	@Override
	public void allowSend(boolean allow) {
		this.toolbar.allowSend(allow);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

}