package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.module.receiptModule.client.userInterface.DASRequestForm;
import bigBang.module.receiptModule.client.userInterface.DASRequestTasksOperationsToolbar;
import bigBang.module.receiptModule.client.userInterface.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.presenter.DASRequestTasksViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.DASRequestTasksViewPresenter.Action;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;

public class DASRequestTasksView extends View implements DASRequestTasksViewPresenter.Display {

	protected DASRequestForm form;
	protected ReceiptForm receiptForm;
	protected DASRequestTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	
	public DASRequestTasksView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		toolbar = new DASRequestTasksOperationsToolbar() {
			
			@Override
			public void onReceiveResponse() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.RECEIVE_RESPONSE));
			}

			@Override
			public void onRepeat() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.REPEAT));
			}
			
			@Override
			public void onCancelRequest(){
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}
		};
		wrapper.add(toolbar);
		
		form = new DASRequestForm();
		form.setReadOnly(true);
		form.setSize("100%", "100%");
		wrapper.add(form.getNonScrollableContent());
		
		wrapper.add(new ListHeader("Recibo"));
		
		this.receiptForm = new ReceiptForm();
		this.receiptForm.setReadOnly(true);
		this.receiptForm.setSize("100%", "100%");
		wrapper.add(this.receiptForm);
		
		wrapper.setCellHeight(this.receiptForm, "100%");
		
		this.overlayContainer = new SimplePanel();
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<DASRequest> getForm() {
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
	public void allowRepeat(boolean allow) {
		toolbar.allowRepeat(allow);
	}
	
	@Override
	public void allowReceiveResponse(boolean allow) {
		toolbar.allowReceiveResponse(allow);
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
					DASRequestTasksView.this.popupPanel = null;
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
	public HasValue<Receipt> getReceiptForm() {
		return this.receiptForm;
	}

}
