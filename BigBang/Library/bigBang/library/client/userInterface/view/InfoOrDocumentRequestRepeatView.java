package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.InfoOrDocumentRequestOperationsToolbar;
import bigBang.library.client.userInterface.form.InfoOrDocumentRequestForm;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestRepeatViewPresenter;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestRepeatViewPresenter.Action;

public class InfoOrDocumentRequestRepeatView extends View implements InfoOrDocumentRequestRepeatViewPresenter.Display {

	private InfoOrDocumentRequestOperationsToolbar toolbar;
	private InfoOrDocumentRequestForm form;
	private ActionInvokedEventHandler<Action> handler;
	
	public InfoOrDocumentRequestRepeatView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new InfoOrDocumentRequestOperationsToolbar() {
			
			@Override
			public void onSendRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<InfoOrDocumentRequestRepeatViewPresenter.Action>(Action.SEND));
			}
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<InfoOrDocumentRequestRepeatViewPresenter.Action>(Action.CANCEL));
			}
		};
		wrapper.add(toolbar);
		
		form = new InfoOrDocumentRequestForm();
		wrapper.add(form.getNonScrollableContent());
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
		this.handler = handler;
	}

	@Override
	public void setAvailableContacts(Contact[] contact) {
		form.setAvailableContacts(contact);
	}

}
