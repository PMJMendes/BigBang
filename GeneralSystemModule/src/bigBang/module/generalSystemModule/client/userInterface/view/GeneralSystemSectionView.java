package bigBang.module.generalSystemModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.shared.userInterface.DockItem;
import bigBang.library.shared.userInterface.DockPanel;
import bigBang.library.shared.userInterface.presenter.OperationViewPresenter;
import bigBang.library.shared.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.presenter.GeneralSystemSectionViewPresenter;;

public class GeneralSystemSectionView extends View implements GeneralSystemSectionViewPresenter.Display {

	private DockPanel operationDock;
	private SimplePanel operationViewContainer;
	
	public GeneralSystemSectionView(){
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
