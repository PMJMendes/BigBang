package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ExternalInfoRequestForm;
import bigBang.library.client.userInterface.ExternalInfoRequestTasksOperationsToolbar;
import bigBang.library.client.userInterface.presenter.ExternalInfoRequestTasksViewPresenter;
import bigBang.library.client.userInterface.presenter.ExternalInfoRequestTasksViewPresenter.Action;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class ExternalInfoRequestTasksView extends View implements ExternalInfoRequestTasksViewPresenter.Display {

	protected ExternalInfoRequestForm form;
	protected ExternalInfoRequestTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	
	public ExternalInfoRequestTasksView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		toolbar = new ExternalInfoRequestTasksOperationsToolbar() {

			@Override
			public void onSendResponse() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.SEND_RESPONSE));
			}

			@Override
			public void onContinue() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CONTINUE));
			}

			@Override
			public void onClose() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CLOSE));
			}

			@Override
			protected void onGoToRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.GO_TO_PROCESS));
			}
			
		};
		wrapper.add(toolbar);
		
		form = new ExternalInfoRequestForm();
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
	public HasValue<ExternalInfoRequest> getForm() {
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
	public void allowSendResponse(boolean allow) {
		toolbar.allowSendResponse(allow);
	}
	
	@Override
	public void allowContinue(boolean allow) {
		toolbar.allowContinue(allow);
	}
	
	@Override
	public void allowClose(boolean allow) {
		toolbar.allowClose(allow);
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
					ExternalInfoRequestTasksView.this.popupPanel = null;
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

}
