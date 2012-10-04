package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.InfoOrDocumentRequestOperationsToolbar;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.form.InfoOrDocumentRequestForm;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestViewPresenter;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestViewPresenter.Action;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class InfoOrDocumentRequestView<T extends ProcessBase> extends View implements InfoOrDocumentRequestViewPresenter.Display<T>{
	
	protected InfoOrDocumentRequestForm form;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected FormView<T> ownerForm;
	
	public InfoOrDocumentRequestView(FormView<T> ownerForm){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		InfoOrDocumentRequestOperationsToolbar toolbar = new InfoOrDocumentRequestOperationsToolbar() {
			
			@Override
			public void onSendRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InfoOrDocumentRequestViewPresenter.Action>(Action.SEND));
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InfoOrDocumentRequestViewPresenter.Action>(Action.CANCEL));
			}
		};

		VerticalPanel ownerWrapper = new VerticalPanel();
		ownerWrapper.setSize("100%", "100%");
		ListHeader ownerHeader = new ListHeader("Ficha de Processo");
		ownerHeader.setHeight("30px");
		ownerWrapper.add(ownerHeader);
		this.ownerForm = ownerForm;
		ownerForm.setReadOnly(true);
		ownerWrapper.add(ownerForm);
		ownerWrapper.setCellHeight(ownerForm, "100%");
		mainWrapper.addWest(ownerWrapper, 665);
		
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
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public HasValue<T> getOwnerForm() {
		return this.ownerForm;
	}

	@Override
	public void setAvailableContacts(Contact[] contacts) {
		this.form.setAvailableContacts(contacts);
	}
	
}