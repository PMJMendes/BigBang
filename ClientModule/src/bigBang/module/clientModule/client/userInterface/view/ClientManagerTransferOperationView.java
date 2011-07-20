package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.presenter.ClientManagerTransferOperationViewPresenter;
import bigBang.module.clientModule.shared.Client;

public class ClientManagerTransferOperationView extends View implements ClientManagerTransferOperationViewPresenter.Display {

	protected static class SelectedList extends FilterableList<Client> {
		
	}
	
	protected static final int SEARCH_PANEL_WIDTH = 300; //PX
	
	protected ClientSearchPanel searchPanel;
	protected SelectedList selectedList; 
	protected ClientManagerFormView managerForm;
	protected ClientFormView clientForm;
	
	public ClientManagerTransferOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		this.searchPanel = new ClientSearchPanel();
		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");

		this.managerForm = new ClientManagerFormView();
		this.clientForm = new ClientFormView();
		this.selectedList = new SelectedList();
		
		contentWrapper.addNorth(this.managerForm, 230);
		contentWrapper.add(selectedList);
		
		mainWrapper.add(contentWrapper);
		
		initWidget(mainWrapper);
	}
	
}
