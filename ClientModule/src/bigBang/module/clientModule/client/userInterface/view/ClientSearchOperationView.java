package bigBang.module.clientModule.client.userInterface.view;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;
import bigBang.module.clientModule.shared.ClientProcess;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class ClientSearchOperationView extends View implements ClientSearchOperationViewPresenter.Display {
	
	private final int SEARCH_PANEL_WIDTH = 400; //minimum and starting width (px)
	private final int SEARCH_PREVIEW_PANEL_WIDTH = 400;
	
	private ClientSearchPanel searchPanel;
	private ClientPreviewPanel previewPanel;
	
	public ClientSearchOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		searchPanel = new ClientSearchPanel();
		searchPanel.setSize("100%", "100%");
		wrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		wrapper.setWidgetMinSize(searchPanel, SEARCH_PANEL_WIDTH);

		previewPanel = new ClientPreviewPanel();
		wrapper.add(previewPanel);
		
		initWidget(wrapper);
	}
	
	public HasValue<String> getClientSearchList() {
		return this.searchPanel;
	}

	public HasValue<ClientProcess> getPreviewWidget() {
		return this.previewPanel;
	}
	
	public View getInstance() {
		return new ClientSearchOperationView();
	}
	
}
