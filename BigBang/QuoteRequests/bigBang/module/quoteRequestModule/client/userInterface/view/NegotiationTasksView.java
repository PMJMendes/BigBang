package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationTasksOperationsToolbar;
import bigBang.module.quoteRequestModule.client.userInterface.form.NegotiationForm;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationTasksViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationTasksViewPresenter.Action;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class NegotiationTasksView extends View implements NegotiationTasksViewPresenter.Display {

	protected NegotiationForm form;
	protected NegotiationTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	
	public NegotiationTasksView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		toolbar = new NegotiationTasksOperationsToolbar() {

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<NegotiationTasksViewPresenter.Action>(Action.CANCEL));
			}

			@Override
			public void onGrant() {
				handler.onActionInvoked(new ActionInvokedEvent<NegotiationTasksViewPresenter.Action>(Action.GRANT));
			}

			@Override
			public void onRepeat() {
				handler.onActionInvoked(new ActionInvokedEvent<NegotiationTasksViewPresenter.Action>(Action.REPEAT_QUOTE_REQUEST));
			}

			@Override
			public void onReceiveResponse() {
				handler.onActionInvoked(new ActionInvokedEvent<NegotiationTasksViewPresenter.Action>(Action.RECEIVE_QUOTE));
			}

			@Override
			protected void onGoToProcess() {
				handler.onActionInvoked(new ActionInvokedEvent<NegotiationTasksViewPresenter.Action>(Action.GO_TO_PROCESS));
			}
			
		};
		wrapper.add(toolbar);
		
		form = new NegotiationForm();
		form.setReadOnly(true);
		form.setSize("100%", "100%");
		wrapper.add(form);
		wrapper.setCellHeight(form, "100%");
		
		this.overlayContainer = new SimplePanel();
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<Negotiation> getForm() {
		return form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		toolbar.hideAll();
		toolbar.setGoToProcessVisible();
	}

	@Override
	public HasWidgets getOverlayViewContainer() {
		return this.overlayContainer;
	}

	@Override
	public void showOverlayViewContainer(boolean show) {
		if(show && this.popupPanel == null){
			this.popupPanel = new PopupPanel(){
				@Override
				protected void onDetach() {
					super.onDetach();
					NegotiationTasksView.this.popupPanel = null;
				}
			};
			this.popupPanel.add((Widget)this.overlayContainer);
		}

		if(this.popupPanel != null){
			if(show && !this.popupPanel.isAttached()){
				this.popupPanel.center();
			}else if(this.popupPanel.isAttached() && !show){
				this.popupPanel.hidePopup();
				this.popupPanel.remove((Widget)this.overlayContainer);
				this.popupPanel = null;
			}
		}
	}

	@Override
	public void allowGrant(boolean allow) {
		toolbar.allowGrant(allow);
	}

	@Override
	public void allowCancel(boolean allow) {
		toolbar.allowCancel(allow);
	}

	@Override
	public void allowReceiveQuote(boolean allow) {
		toolbar.allowReceiveResponse(allow);
	}

	@Override
	public void allowRepeatQuoteRequest(boolean allow) {
		toolbar.allowReceiveResponse(allow);
	}
}
