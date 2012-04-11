package bigBang.module.casualtyModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.DockItem;
import bigBang.library.client.userInterface.DockPanel;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySectionViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySectionViewPresenter.SectionOperation;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySectionViewPresenter.Action;

public class CasualtySectionView extends View implements CasualtySectionViewPresenter.Display {
	private DockPanel operationDock;
	private SimplePanel operationViewContainer;
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	private ActionInvokedEventHandler<Action> actionHandler;
	private ActionInvokedEventHandler<SectionOperation> operationSelectionHandler;

	public CasualtySectionView(){
		VerticalPanel panel = new VerticalPanel();
		initWidget(panel);
		panel.setSize("100%", "100%");

		this.operationDock = new DockPanel();
		panel.add(this.operationDock);
		initializeDock();
		this.operationDock.addValueChangeHandler(new ValueChangeHandler<Object>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Object> event) {
				operationSelectionHandler.onActionInvoked(new ActionInvokedEvent<CasualtySectionViewPresenter.SectionOperation>((SectionOperation)event.getValue()));
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
		addDockItem("Pesquisa", null, SectionOperation.OPERATIONS);
		addDockItem("Transf. Gestor", null, SectionOperation.MASS_MANAGER_TRANSFER);
	}

	protected void addDockItem(String text, AbstractImagePrototype icon, final CasualtySectionViewPresenter.SectionOperation action){
		if(icon == null)
			icon = MessageBox.MESSAGEBOX_IMAGES.dialogInformation();
		DockItem item = new DockItem(text, icon, action);
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
					CasualtySectionView.this.popupPanel = null;
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
