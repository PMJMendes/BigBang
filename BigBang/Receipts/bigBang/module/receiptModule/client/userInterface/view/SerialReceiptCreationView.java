package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.definitions.shared.ScanHandle;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.HasNavigationStateChangedHandlers;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.shared.ScanItem;
import bigBang.module.receiptModule.client.userInterface.ReceiptImagePanel;
import bigBang.module.receiptModule.client.userInterface.SerialReceiptCreationToolbar;
import bigBang.module.receiptModule.client.userInterface.form.SerialReceiptCreationForm;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter.Action;
import bigBang.module.receiptModule.shared.ReceiptOwnerWrapper;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SerialReceiptCreationView extends View implements SerialReceiptCreationViewPresenter.Display{

	private SplitLayoutPanel wrapper;
	private ReceiptImagePanel receiptPanel;
	private SerialReceiptCreationForm form;
	private SerialReceiptCreationToolbar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	private boolean hasImage = true;
	private Button hasImageButton;

	public SerialReceiptCreationView(){
		wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");


		VerticalPanel listWrapper = new VerticalPanel();
		hasImageButton = new Button("Sem imagem");

		hasImageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(hasImage){
					hasImageButton.setText("Com imagem");
					hasImage = false;
					actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.NO_IMAGE));
				}
				else{
					hasImageButton.setText("Sem imagem");
					hasImage = true;
					actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.IMAGE));
				}
			}
		});

		listWrapper.setSize("100%", "100%");
		receiptPanel = new ReceiptImagePanel();
		receiptPanel.getNavigationPanel().navBar.setRightWidget(hasImageButton);
		listWrapper.add(receiptPanel);
		receiptPanel.setSize("100%", "100%");
		listWrapper.setCellHeight(receiptPanel, "100%");
		wrapper.addWest(listWrapper, 600);

		VerticalPanel right = new VerticalPanel();
		ListHeader rightHeader = new ListHeader("Recibo");

		toolbar = new SerialReceiptCreationToolbar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.CANCEL));

			}

			@Override
			public void saveReceipt() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.SAVE));
			}
		};

		right.setSize("100%", "100%");
		form = new SerialReceiptCreationForm(){

			@Override
			protected void onClickMarkAsInvalid() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.MARK_RECEIPT));
			}

			@Override
			protected void onClickVerifyPolicyNumber() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.VERIFY_POLICY));
			}

			@Override
			protected void onClickVerifyReceiptNumber() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.VERIFY_RECEIPT));				
			}

			@Override
			protected void onEnterKeyReceiptNumber() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.VERIFY_RECEIPT));		
			}

			@Override
			protected void onEnterKeyPolicyNumber() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.VERIFY_POLICY));
			}

			@Override
			protected void onClickNewReceipt() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.NEW_RECEIPT));
			}

			@Override
			protected void onChangedReceiptNumber() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.CHANGED_RECEIPT_NUMBER));
			}

			@Override
			protected void onChangedPolicyNumber() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.CHANGED_POLICY_NUMBER));
			}

			@Override
			protected void onOwnerTypeChanged() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.CHANGED_OWNER_TYPE));
			}

			@Override
			protected void onSubPolicyChanged() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.CHANGED_SUB_POLICY));
			}

			@Override
			protected void onSubCasualtyChanged() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialReceiptCreationViewPresenter.Action>(Action.CHANGED_SUB_CASUALTY));
			}
		};
		
		right.add(rightHeader);
		rightHeader.setSize("100%", "21px");
		right.add(toolbar);
		right.add(form); 
		right.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		right.setCellHeight(form, "100%");
		toolbar.setEnabled(false);
		form.setReadOnly(true);

		wrapper.add(right);
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
	public HasEditableValue<ReceiptOwnerWrapper> getForm() {
		return form;
	}

	@Override
	public void enableReceiptNumber(boolean enable){
		form.setReceiptNumberReadOnly(!enable);
	}

	@Override
	public void enableNewReceipt(boolean enable){
		form.newReceiptEnabled(enable);
	}

	@Override
	public void clear() {
		form.showLabel(false);
		form.showNotAvailableLabel(false);
		form.showImageAlreadyDefineWarning(false);
		form.clearInfo();
		form.setReceiptReadOnly(true);
		toolbar.setEnabled(false);
		form.setPolicyReadOnly(true);
		form.newReceiptEnabled(false);
		form.enableOwner(false);
	}

	@Override
	public void setReceiptNumber(String id, boolean keepCursorPos){
		form.setReceiptNumber(id, keepCursorPos);
	}

	@Override
	public void lockReceiptView(boolean b) {

		if(!b){
			receiptPanel.navigateToDirectoryList(null);
		}else{
			receiptPanel.showNoImageReceipt();
		}
	}

	@Override
	public String getReceiptNumber() {
		return form.getReceiptNumber();
	}

	@Override
	public void hideMarkAsEnable(boolean b) {
		form.hideMarkAsEnable(b);
	}

	@Override
	public void enableToolbar(boolean b) {
		toolbar.setEnabled(b);
	}

	@Override
	public void setReceiptReadOnly(boolean b) {
		form.setReceiptReadOnly(b);
	}

	@Override
	public HasNavigationStateChangedHandlers getNavigationPanel() {
		return receiptPanel.getNavigationPanel();
	}

	@Override
	public void enableOwner(boolean b) {
		form.enableOwner(b);
	}

	@Override
	public void setFocusOnPolicy() {
		form.setFocusOnPolicy();
	}

	@Override
	public void setFocusOnReceipt() {
		form.setFocusOnReceipt();	

	}

	@Override
	public ScanItem getSelectedScanItem() {
		return receiptPanel.getCurrentItem();
	}

	@Override
	public void removeScanItem(ScanHandle handle) {

		if(handle!= null){
			receiptPanel.removeSelected(handle.handle);
		}

	}

	@Override
	public void panelNavigateBack() {
		receiptPanel.getNavigationPanel().navigateBack();
	}

	@Override
	public void markReceipt(ScanItem currentItem) {
		receiptPanel.markReceipt(currentItem.handle);
	}

	@Override
	public String getPolicyNumber() {
		return form.getPolicyNumber();
	}

	@Override
	public String getSubPolicyId() {
		return form.getSubPolicyId();
	}

	@Override
	public String getSubCasualtyId() {
		return form.getSubCasualtyId();
	}

	@Override
	public void clearOwner() {
		form.clearOwner();
	}

	@Override
	public void setPolicyNumber(String policyNumber, boolean keepCursorPos) {
		form.setPolicyNumber(policyNumber, keepCursorPos);
	}

	@Override
	public void enableOwnerProblem(boolean b){
		form.isPolicyNumberProblem(b);
		form.showLabel(b);
	}
	
	@Override
	public void enableMarkReceipt(boolean b) {
		form.enableMarkAsInvalid(b);
	}

	@Override
	public void showImageAlreadyDefinedWarning(boolean hasImage) {
		this.form.showImageAlreadyDefineWarning(hasImage);
	}

	@Override
	public void setOwnerNotAvailable(boolean b) {
		form.isPolicyNumberProblem(b);
		form.showNotAvailableLabel(b);
	}

}
