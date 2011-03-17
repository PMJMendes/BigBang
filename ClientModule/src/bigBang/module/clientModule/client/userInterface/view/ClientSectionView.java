package bigBang.module.clientModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.library.shared.userInterface.DockItem;
import bigBang.library.shared.userInterface.DockPanel;
import bigBang.library.shared.userInterface.presenter.OperationViewPresenter;
import bigBang.library.shared.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSectionViewPresenter;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClientSectionView extends View implements ClientSectionViewPresenter.Display {
	
	private DockPanel operationDock;
	private SimplePanel operationViewContainer;
	
	public ClientSectionView(){
		VerticalPanel panel = new VerticalPanel();
		panel.setSize("100%", "100%");

		this.operationDock = new DockPanel();
		panel.add(this.operationDock);
		
		this.operationViewContainer = new SimplePanel();
		this.operationViewContainer.setSize("100%", "100%");
		panel.add(operationViewContainer);
		panel.setCellHeight(operationViewContainer, "100%");
		
		initWidget(panel);
	}

	public void createOperationNavigationItem(OperationViewPresenter p) {
		DockItem item = new DockItem(p.getOperation().getShortDescription(), MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), null, p);
		item.setTitle(p.getOperation().getDescription());
		item.setSize("100px", "52px");
		this.operationDock.addItem(item);
	}

	public HasValue <Object> getOperationNavigationPanel() {
		return operationDock;
	}

	public HasWidgets getOperationViewContainer() {
		return operationViewContainer;
	}

	public void selectOperation(OperationViewPresenter p) {
		this.operationDock.setValue(p);
	}
}
