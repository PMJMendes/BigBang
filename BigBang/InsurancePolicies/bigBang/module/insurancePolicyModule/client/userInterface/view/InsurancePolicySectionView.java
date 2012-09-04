package bigBang.module.insurancePolicyModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.DockItem;
import bigBang.library.client.userInterface.DockPanel;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.resources.Resources;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySectionViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySectionViewPresenter.Action;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySectionViewPresenter.SectionOperation;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class InsurancePolicySectionView extends View implements InsurancePolicySectionViewPresenter.Display {
	
	private DockPanel operationDock;
	private SimplePanel operationViewContainer;
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	private ActionInvokedEventHandler<Action> actionHandler;
	private ActionInvokedEventHandler<SectionOperation> operationSelectionHandler;

	public InsurancePolicySectionView(){
		VerticalPanel panel = new VerticalPanel();
		initWidget(panel);
		panel.setSize("100%", "100%");

		this.operationDock = new DockPanel();
		panel.add(this.operationDock);
		initializeDock();
		this.operationDock.addValueChangeHandler(new ValueChangeHandler<Object>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Object> event) {
				operationSelectionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySectionViewPresenter.SectionOperation>((SectionOperation)event.getValue()));
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
		addDockItem("Transf. Gestor", r.managerIcon(), SectionOperation.MASS_MANAGER_TRANSFER);
		addDockItem("Relatórios", r.reportIcon(), SectionOperation.REPORT);
	}

	protected void addDockItem(String text, ImageResource icon, final InsurancePolicySectionViewPresenter.SectionOperation action){
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
					InsurancePolicySectionView.this.popupPanel = null;
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
