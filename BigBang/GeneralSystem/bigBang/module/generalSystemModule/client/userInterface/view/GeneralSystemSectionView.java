package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.DockPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.presenter.GeneralSystemSectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.GeneralSystemSectionViewPresenter.Action;
import bigBang.module.generalSystemModule.client.userInterface.presenter.GeneralSystemSectionViewPresenter.SectionOperation;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GeneralSystemSectionView extends View implements GeneralSystemSectionViewPresenter.Display {

	private DockPanel operationDock;
	private SimplePanel operationViewContainer;
	
	public GeneralSystemSectionView(){
		VerticalPanel panel = new VerticalPanel();
		initWidget(panel);
		panel.setSize("100%", "100%");

		this.operationDock = new DockPanel();
		panel.add(this.operationDock);
		
		this.operationViewContainer = new SimplePanel();
		this.operationViewContainer.setSize("100%", "100%");
		panel.add(operationViewContainer);
		panel.setCellHeight(operationViewContainer, "100%");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showOverlayViewContainer(boolean show) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectOperation(SectionOperation operation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerOperationSelectionHandler(
			ActionInvokedEventHandler<SectionOperation> handler) {
		// TODO Auto-generated method stub
		
	}

//	public void selectOperation(OperationViewPresenter p) {
//		this.operationDock.setValue(p);
//	}
	
}
