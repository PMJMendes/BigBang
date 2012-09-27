package bigBang.module.receiptModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.DockItem;
import bigBang.library.client.userInterface.DockPanel;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.resources.Resources;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSectionViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSectionViewPresenter.Action;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSectionViewPresenter.SectionOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReceiptSectionView extends View implements ReceiptSectionViewPresenter.Display{

	private DockPanel operationDock;
	private SimplePanel operationViewContainer;
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	private ActionInvokedEventHandler<Action> actionHandler;
	private ActionInvokedEventHandler<SectionOperation> operationSelectionHandler;

	public ReceiptSectionView(){
		VerticalPanel panel = new VerticalPanel();
		initWidget(panel);
		panel.setSize("100%", "100%");

		this.operationDock = new DockPanel();
		panel.add(this.operationDock);
		initializeDock();
		this.operationDock.addValueChangeHandler(new ValueChangeHandler<Object>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Object> event) {
				operationSelectionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSectionViewPresenter.SectionOperation>((SectionOperation)event.getValue()));
			}
		});

		this.operationViewContainer = new SimplePanel();
		this.operationViewContainer.setSize("100%", "100%");
		panel.add(operationViewContainer);
		panel.setCellHeight(operationViewContainer, "100%");

		this.overlayContainer = new SimplePanel();
	}

	@Override
	protected void initializeView() {
		return;
	}

	public void initializeDock() {
		Resources r = GWT.create(Resources.class);
		
		addDockItem("Pesquisa", r.searchIcon(), SectionOperation.OPERATIONS);
		addDockItem("Criação em Série", r.massCreationIcon(), SectionOperation.SERIAL_RECEIPT_CREATION);
		addDockItem("Avisos de Cobrança", r.sendReceiptIcon(), SectionOperation.MASS_CREATE_PAYMENT_NOTICE);
		addDockItem("Pedidos de Assinatura", r.signatureRequestIcon(), SectionOperation.MASS_SIGNATURE_REQUEST);
		addDockItem("Cobranças", r.paymentIcon(), SectionOperation.SERIAL_RECEIPT_MARK_FOR_PAYMENT);
		addDockItem("Prestações de Contas", r.accountabilityIcon(), SectionOperation.MASS_INSURER_ACCOUNTING);
		addDockItem("Retrocessões", r.accountabilityIcon(), SectionOperation.MASS_AGENT_ACCOUNTING);
		addDockItem("Envio dos Recibos", r.sendReceiptIcon(), SectionOperation.MASS_SEND_RECEIPT_TO_CLIENT);
		addDockItem("Devoluções à Seguradora", r.returnIcon(), SectionOperation.MASS_RETURN_TO_INSURER);
		addDockItem("Relatórios", r.reportIcon(), SectionOperation.REPORT);
		addDockItem("Importações / Outros", r.generalTasksIcon(), SectionOperation.GENERAL_TASKS);
	}

	protected void addDockItem(String text, ImageResource icon, final ReceiptSectionViewPresenter.SectionOperation action){
		DockItem item = null;

		if(icon == null){
			item = new DockItem(text, MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), action);
		}else{
			item = new DockItem(text, icon, action);
		}

		item.setTitle(text);
		this.operationDock.addItem(item);
	}

	public HasWidgets getOperationViewContainer() {
		return operationViewContainer;
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
					actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.ON_OVERLAY_CLOSED));
					ReceiptSectionView.this.popupPanel = null;
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
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler){
		this.actionHandler = handler;
	}

	@Override
	public void selectOperation(SectionOperation operation) {
		this.operationDock.setValue(operation, false);
	}

	@Override
	public void registerOperationSelectionHandler(
			ActionInvokedEventHandler<SectionOperation> handler) {
		this.operationSelectionHandler = handler;
	}

}
