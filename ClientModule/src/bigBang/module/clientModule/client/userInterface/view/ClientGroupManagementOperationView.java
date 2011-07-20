package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.presenter.ClientGroupManagementOperationViewPresenter;
import bigBang.module.clientModule.shared.Client;

public class ClientGroupManagementOperationView extends View implements ClientGroupManagementOperationViewPresenter.Display{

protected static class SelectedList extends FilterableList<Client> {
		
	}
	
	protected static final int SEARCH_PANEL_WIDTH = 300; //PX
	
	protected ClientSearchPanel searchPanel;
	protected SelectedList selectedList; 
	protected ClientGroupFormView groupForm;
	protected ClientFormView clientForm;
	
	public ClientGroupManagementOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		this.searchPanel = new ClientSearchPanel();
		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");

		this.groupForm = new ClientGroupFormView();
		this.clientForm = new ClientFormView();
		this.selectedList = new SelectedList();
		
		contentWrapper.addNorth(this.groupForm, 100);
		contentWrapper.add(selectedList);
		
		mainWrapper.add(contentWrapper);
		
		initWidget(mainWrapper);
	}
	
}
