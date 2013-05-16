package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Negotiation.Cancellation;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationCancellationToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.form.NegotiationCancellationForm;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationCancellationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationCancellationViewPresenter.Action;

import com.google.gwt.user.client.ui.VerticalPanel;

public class NegotiationCancellationView extends View implements NegotiationCancellationViewPresenter.Display{
	
	private NegotiationCancellationToolBar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	private NegotiationCancellationForm form;
	
	public NegotiationCancellationView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new NegotiationCancellationToolBar() {
			
			@Override
			public void onCancelNegotiationRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<NegotiationCancellationViewPresenter.Action>(Action.CANCEL_NEGOTIATION));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<NegotiationCancellationViewPresenter.Action>(Action.CANCEL));
			}
		};
		
		form = new NegotiationCancellationForm();
		
		toolbar.setWidth("100%");
		toolbar.setHeight("21px"); 
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
	public HasEditableValue<Cancellation> getForm() {
		return form;
	}

	@Override
	public void setAvailableContacts(Contact[] result) {
		form.setAvailableContacts(result);
	}
	
	@Override
	public void setUserList(String[] displayNames){
		form.setUserList(displayNames);
	}

	@Override
	public void clear() {
		
		form.clearInfo();
		
	}

}
