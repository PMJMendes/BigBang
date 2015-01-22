package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.module.receiptModule.client.userInterface.form.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.form.SignatureRequestForm;
import bigBang.module.receiptModule.client.userInterface.presenter.SignatureRequestTasksViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SignatureRequestTasksViewPresenter.Action;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.SignatureRequestTasksOperationsToolbar;

public class SignatureRequestTasksView extends View implements SignatureRequestTasksViewPresenter.Display {

	protected SignatureRequestForm form;
	protected SignatureRequestTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	protected ReceiptForm receiptForm;
	
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	
	public SignatureRequestTasksView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		toolbar = new SignatureRequestTasksOperationsToolbar() {

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<SignatureRequestTasksViewPresenter.Action>(Action.CANCEL));
			}

			@Override
			public void onReceive() {
				handler.onActionInvoked(new ActionInvokedEvent<SignatureRequestTasksViewPresenter.Action>(Action.RECEIVE));
			}

			@Override
			public void onRepeat() {
				handler.onActionInvoked(new ActionInvokedEvent<SignatureRequestTasksViewPresenter.Action>(Action.REPEAT));
			}

			@Override
			protected void onGoToProcess() {
				handler.onActionInvoked(new ActionInvokedEvent<SignatureRequestTasksViewPresenter.Action>(Action.GO_TO_PROCESS));
			}
		};
		wrapper.add(toolbar);
		
		form = new SignatureRequestForm();
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
	public HasValue<SignatureRequest> getForm() {
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
	public void allowReceive(boolean allow) {
		toolbar.allowReceive(allow);
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
					SignatureRequestTasksView.this.popupPanel = null;
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
