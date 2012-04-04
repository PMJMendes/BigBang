package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSectionViewPresenter;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.DockPanel;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;


import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuoteRequestSectionView extends View implements QuoteRequestSectionViewPresenter.Display {
	
	private DockPanel operationDock;
	private SimplePanel operationViewContainer;
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	private ActionInvokedEventHandler<QuoteRequestSectionViewPresenter.Action> actionHandler;

	public QuoteRequestSectionView(){
		VerticalPanel panel = new VerticalPanel();
		initWidget(panel);
		panel.setSize("100%", "100%");

		this.operationDock = new DockPanel();
		panel.add(this.operationDock);

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

	//	public void createOperationNavigationItem(OperationViewPresenter p, boolean enabled) {
	//		AbstractImagePrototype icon = p.getOperation().getIcon();
	//		if(icon == null)
	//			icon = MessageBox.MESSAGEBOX_IMAGES.dialogInformation();
	//		DockItem item = new DockItem(p.getOperation().getShortDescription(), icon, null, p);
	//		item.setEnabled(enabled);
	//		item.setTitle(p.getOperation().getDescription());
	//		item.setSize("100px", "52px");
	//		this.operationDock.addItem(item);
	//	}

	public HasValue <Object> getOperationNavigationPanel() {
		return operationDock;
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
					actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSectionViewPresenter.Action>(QuoteRequestSectionViewPresenter.Action.ON_OVERLAY_CLOSED));
					QuoteRequestSectionView.this.popupPanel = null;
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

	//	public void selectOperation(OperationViewPresenter p) {
	//		this.operationDock.setValue(p);
	//	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<QuoteRequestSectionViewPresenter.Action> handler){
		this.actionHandler = handler;
	}

}
