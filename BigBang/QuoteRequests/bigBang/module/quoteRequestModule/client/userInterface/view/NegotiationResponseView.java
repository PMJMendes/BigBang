package bigBang.module.quoteRequestModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Negotiation.Response;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationResponseToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.form.NegotiationResponseForm;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationResponseViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationResponseViewPresenter.Action;


public class NegotiationResponseView extends View implements NegotiationResponseViewPresenter.Display {

	private NegotiationResponseToolBar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	private NegotiationResponseForm form;
	
	public NegotiationResponseView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new NegotiationResponseToolBar() {
			
			@Override
			protected void onResponse() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<NegotiationResponseViewPresenter.Action>(Action.NEGOTIATION_RESPONSE));
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<NegotiationResponseViewPresenter.Action>(Action.CANCEL));
			}
		};
		
		form = new NegotiationResponseForm();
		
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
	public HasEditableValue<Response> getForm() {
		return form;
	}

	@Override
	public void clear() {
		form.clearInfo();
	}

}
