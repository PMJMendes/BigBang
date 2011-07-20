package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySectionViewPresenter;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.library.client.userInterface.DockItem;
import bigBang.library.client.userInterface.DockPanel;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InsurancePolicySectionView extends View implements InsurancePolicySectionViewPresenter.Display {
	
	private DockPanel operationDock;
	private SimplePanel operationViewContainer;
	
	public InsurancePolicySectionView(){
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

	public void createOperationNavigationItem(OperationViewPresenter p, boolean enabled) {
		AbstractImagePrototype icon = p.getOperation().getIcon();
		if(icon == null)
			icon = MessageBox.MESSAGEBOX_IMAGES.dialogInformation();
		DockItem item = new DockItem(p.getOperation().getShortDescription(), icon, null, p);
		item.setEnabled(enabled);
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
