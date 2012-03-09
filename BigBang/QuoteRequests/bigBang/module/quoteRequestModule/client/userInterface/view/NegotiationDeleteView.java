package bigBang.module.quoteRequestModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Negotiation.Deletion;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationDeleteForm;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationDeleteToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationDeleteViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationDeleteViewPresenter.Action;

public class NegotiationDeleteView extends View implements NegotiationDeleteViewPresenter.Display{
	
	private NegotiationDeleteForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public NegotiationDeleteView(){
		
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		this.form = new NegotiationDeleteForm();
		
		NegotiationDeleteToolBar toolbar = new NegotiationDeleteToolBar() {
			
			@Override
			public void onDeleteRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<NegotiationDeleteViewPresenter.Action>(Action.DELETE));
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<NegotiationDeleteViewPresenter.Action>(Action.CANCEL));
			}
		};
		
		wrapper.add(toolbar);
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Deletion> getForm() {
		return form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void clear() {
		form.clearInfo();
	}

}
