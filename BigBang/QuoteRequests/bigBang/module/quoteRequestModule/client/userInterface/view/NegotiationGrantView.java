package bigBang.module.quoteRequestModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.quoteRequestModule.client.userInterface.NegotiationGrantForm;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationGrantToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationGrantViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationGrantViewPresenter.Action;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Negotiation.Grant;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;

public class NegotiationGrantView extends View implements NegotiationGrantViewPresenter.Display{


	private NegotiationGrantToolBar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	private NegotiationGrantForm form;

	public NegotiationGrantView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);

		toolbar = new NegotiationGrantToolBar(){

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<NegotiationGrantViewPresenter.Action>(Action.CANCEL));
			}

			@Override
			public void onGrantNegotiationRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<NegotiationGrantViewPresenter.Action>(Action.GRANT_NEGOTIATION));

			}

		};

		form = new NegotiationGrantForm();
		toolbar.setWidth("100");
		form.setSize("100%", "100%");

		wrapper.add(toolbar);
		wrapper.add(form);
		wrapper.setSize("700px", "650px");
		wrapper.setCellHeight(form, "100%");

	}

	@Override
	protected void initializeView() {
		return;		
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;

	}

	@Override
	public HasEditableValue<Grant> getForm() {
		return form;
	}

	@Override
	public void clear() {
		form.clearInfo();
	}
	
	@Override
	public void setAvailableContacts(Contact[] result) {
		form.setAvailableContacts(result);
	}
	
	@Override
	public void setUserList(String[] displayNames){
		form.setUserList(displayNames);
	}

}
