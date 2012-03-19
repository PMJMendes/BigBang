package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ExternalInfoRequest.Outgoing;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ExternalRequestReplyForm;
import bigBang.library.client.userInterface.ExternalRequestReplyToolbar;
import bigBang.library.client.userInterface.presenter.ExternalRequestReplyViewPresenter;
import bigBang.library.client.userInterface.presenter.ExternalRequestReplyViewPresenter.Action;

public class ExternalRequestReplyView extends View implements ExternalRequestReplyViewPresenter.Display {

	protected ActionInvokedEventHandler<Action> handler;
	protected ExternalRequestReplyForm form;
	
	public ExternalRequestReplyView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		ExternalRequestReplyToolbar toolbar = new ExternalRequestReplyToolbar() {
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ExternalRequestReplyViewPresenter.Action>(Action.CANCEL));
			}
			
			@Override
			public void onSend() {
				handler.onActionInvoked(new ActionInvokedEvent<ExternalRequestReplyViewPresenter.Action>(Action.SEND));
			}
		};
		wrapper.add(toolbar);
		
		form = new ExternalRequestReplyForm();
		form.setReadOnly(false);
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Outgoing> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void setAvailableContacts(Contact[] contacts) {
		form.setAvailableContacts(contacts);
	}

	@Override
	public void setUserNames(String[] usernames) {
		form.setUserList(usernames);
	}

}
