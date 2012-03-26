package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.ReceiptImagePanel;
import bigBang.module.receiptModule.client.userInterface.SerialReceiptCreationForm;
import bigBang.module.receiptModule.client.userInterface.SerialReceiptCreationToolbar;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter.Action;
import bigBang.module.receiptModule.shared.ReceiptPolicyWrapper;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
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
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onEnterKeyPolicyNumber() {
				// TODO Auto-generated method stub
				
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
	public HasValue<ReceiptPolicyWrapper> getForm() {
		return form;
	}
	
	@Override
	public void enableReceiptNumber(boolean enable){
		form.setReceiptNumberReadOnly(!enable);
	}

	@Override
	public void clear() {
		form.clearInfo();
	}

	@Override
	public void lockReceiptView(boolean b) {
		
		if(!b){
			receiptPanel.navigateToDirectoryList(null);
		}else{
			receiptPanel.showNoImageReceipt();
		}
	}

}
