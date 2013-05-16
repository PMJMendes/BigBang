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
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReceiptSectionView extends View implements ReceiptSectionViewPresenter.Display{

	private DockPanel operationDock;
	private DockPanel operationDock2;
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
		this.operationDock2 = new DockPanel();
		this.operationDock2.getElement().getStyle().setBorderWidth(1, Unit.PX);
		panel.add(this.operationDock2);

		this.operationDock.addValueChangeHandler(new ValueChangeHandler<Object>() {

			@Override
			public void onValueChange(ValueChangeEvent<Object> event) {
				if(event.getValue() != null){
					operationSelectionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSectionViewPresenter.SectionOperation>((SectionOperation)event.getValue()));
					operationDock2.setValue(null);
				}
			}
		});


		this.operationDock2.addValueChangeHandler(new ValueChangeHandler<Object>() {

			@Override
			public void onValueChange(ValueChangeEvent<Object> event) {
				if(event.getValue() != null){
					operationSelectionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSectionViewPresenter.SectionOperation>((SectionOperation)event.getValue()));
					operationDock.setValue(null);
				}

			}
		});

		initializeDock();
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

		addDockItem("Pesquisa", r.searchIcon(), SectionOperation.OPERATIONS, 1);
		addDockItem("Criação em Série", r.massCreationIcon(), SectionOperation.SERIAL_RECEIPT_CREATION, 2);
		addDockItem("Avisos de Cobrança", r.sendReceiptIcon(), SectionOperation.MASS_CREATE_PAYMENT_NOTICE, 2);
		addDockItem("Segundos Avisos de Cobrança", r.sendSecondReceiptIcon(), SectionOperation.MASS_CREATE_SECOND_PAYMENT_NOTICE, 2);
		addDockItem("Pedidos de Assinatura", r.signatureRequestIcon(), SectionOperation.MASS_SIGNATURE_REQUEST, 2);
		addDockItem("Cobranças", r.paymentIcon(), SectionOperation.SERIAL_RECEIPT_MARK_FOR_PAYMENT, 2);
		addDockItem("Emissão de Recibos", r.receiptGenerationIcon(), SectionOperation.MASS_RECEIPT_GENERATION, 2);
		addDockItem("Prestações de Contas", r.accountabilityIcon(), SectionOperation.MASS_INSURER_ACCOUNTING, 2);
		addDockItem("Retrocessões", r.accountabilityIcon(), SectionOperation.MASS_AGENT_ACCOUNTING, 2);
		addDockItem("Envio dos Recibos", r.sendReceiptIcon(), SectionOperation.MASS_SEND_RECEIPT_TO_CLIENT, 2);
		addDockItem("Envio de Pagamentos", r.sendPaymentIcon(), SectionOperation.MASS_SEND_PAYMENT, 2);
		addDockItem("Devoluções à Seguradora", r.returnIcon(), SectionOperation.MASS_RETURN_TO_INSURER, 2);
		addDockItem("Relatórios", r.reportIcon(), SectionOperation.REPORT, 1);
		addDockItem("Importações / Outros", r.generalTasksIcon(), SectionOperation.GENERAL_TASKS, 1);

	}

	protected void addDockItem(String text, ImageResource icon, final ReceiptSectionViewPresenter.SectionOperation action,int dockIndex){
		DockItem item = null;

		if(icon == null){
			item = new DockItem(text, MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), action);
		}else{
			item = new DockItem(text, icon, action);
		}

		item.setTitle(text);

		if(dockIndex == 1)
			this.operationDock.addItem(item);

		else 
			this.operationDock2.addItem(item);

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
		this.operationDock2.setValue(operation, false);
	}

	@Override
	public void registerOperationSelectionHandler(
			ActionInvokedEventHandler<SectionOperation> handler) {
		this.operationSelectionHandler = handler;
	}

}
