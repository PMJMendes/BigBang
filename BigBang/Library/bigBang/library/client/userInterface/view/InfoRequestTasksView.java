package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.InfoRequestTasksOperationsToolbar;
import bigBang.library.client.userInterface.presenter.InfoRequestTasksViewPresenter;
import bigBang.library.client.userInterface.presenter.InfoRequestTasksViewPresenter.Action;

public class InfoRequestTasksView extends View implements InfoRequestTasksViewPresenter.Display{

	protected InfoOrDocumentRequestForm form;
	protected InfoRequestTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	
	public InfoRequestTasksView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		toolbar = new InfoRequestTasksOperationsToolbar() {

			@Override
			public void onReceiveResponse() {
				handler.onActionInvoked(new ActionInvokedEvent<InfoRequestTasksViewPresenter.Action>(Action.RECEIVE_RESPONSE));
			}

			@Override
			public void onRepeat() {
				handler.onActionInvoked(new ActionInvokedEvent<InfoRequestTasksViewPresenter.Action>(Action.REPEAT));
			}

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<InfoRequestTasksViewPresenter.Action>(Action.CANCEL));
			}
			
		};
		wrapper.add(toolbar);
		
		form = new InfoOrDocumentRequestForm();
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
	public HasValue<InfoOrDocumentRequest> getForm() {
		return form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		toolbar.hideAll();
	}

	@Override
	public void allowReceiveResponse(boolean allow) {
		toolbar.allowReceiveResponse(allow);
	}
	
	@Override
	public void allowRepeat(boolean allow) {
		toolbar.allowRepeat(allow);
	}
	
	@Override
	public void allowCancel(boolean allow) {
		toolbar.allowCancel(allow);
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
					InfoRequestTasksView.this.popupPanel = null;
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
