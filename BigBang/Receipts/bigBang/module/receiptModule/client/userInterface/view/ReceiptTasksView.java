package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.definitions.shared.ImageItem;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Rectangle;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.ReceiptImageHandlerPanel;
import bigBang.module.receiptModule.client.userInterface.ReceiptTasksOperationsToolbar;
import bigBang.module.receiptModule.client.userInterface.form.ReceiptTasksForm;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptTasksViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptTasksViewPresenter.Action;
import bigBang.module.receiptModule.interfaces.ReceiptService;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReceiptTasksView extends View implements ReceiptTasksViewPresenter.Display {

	protected ReceiptTasksForm form;
	protected ReceiptTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	protected ReceiptImageHandlerPanel imagePanel;

	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;

	public ReceiptTasksView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		toolbar = new ReceiptTasksOperationsToolbar() {

			@Override
			public void onCreateDASRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ReceiptTasksViewPresenter.Action>(Action.CREATE_DAS_REQUEST));
			}

			@Override
			public void onMarkDASUnnecessary() {
				handler.onActionInvoked(new ActionInvokedEvent<ReceiptTasksViewPresenter.Action>(Action.MARK_DAS_UNNECESSARY));
			}

			@Override
			public void onValidate() {
				handler.onActionInvoked(new ActionInvokedEvent<ReceiptTasksViewPresenter.Action>(Action.VALIDATE));
			}

			@Override
			public void onSetForReturn() {
				handler.onActionInvoked(new ActionInvokedEvent<ReceiptTasksViewPresenter.Action>(Action.SET_FOR_RETURN));
			}

			@Override
			protected void onGoToProcess() {
				handler.onActionInvoked(new ActionInvokedEvent<ReceiptTasksViewPresenter.Action>(Action.GO_TO_PROCESS));
			}
		};
		wrapper.add(toolbar);

		form = new ReceiptTasksForm();
		form.setReadOnly(true);
		form.setSize("100%", "100%");

		imagePanel = new ReceiptImageHandlerPanel();
		imagePanel.setSize("100%", "100%");
		imagePanel.setImageService(ReceiptService.Util.getInstance());

		SplitLayoutPanel formWrapper = new SplitLayoutPanel();
		formWrapper.setSize("100%", "100%");

		formWrapper.addWest(form, 650);
		formWrapper.add(imagePanel);

		wrapper.add(formWrapper);
		wrapper.setCellHeight(formWrapper, "100%");

		this.imagePanel.setImageService(ReceiptService.Util.getInstance());
		
		this.overlayContainer = new SimplePanel();
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Receipt> getForm() {
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
	public void allowCreateDASRequest(boolean allow) {
		toolbar.allowCreateDASRequest(allow);
	}

	@Override
	public void allowMarkDASUnnecessary(boolean allow) {
		toolbar.allowMarkDASUnnecessary(allow);
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
					ReceiptTasksView.this.popupPanel = null;
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
	public void allowValidate(boolean b) {
		toolbar.allowValidate(b);

	}

	@Override
	public void allowSetForReturn(boolean b) {
		toolbar.allowSetForReturn(b);

	}

	@Override
	public void clearImages() {
		this.imagePanel.handleImageItem(null);
	}

	@Override
	public void handleImageItem(ImageItem item) {
		this.imagePanel.handleImageItem(item);
	}

	@Override
	public Rectangle getImageSelection() {
		return this.imagePanel.getSelection();
	}
	
	@Override
	public void showCropOption(boolean show){
		this.imagePanel.showCutOption(show);
	}
	
}
