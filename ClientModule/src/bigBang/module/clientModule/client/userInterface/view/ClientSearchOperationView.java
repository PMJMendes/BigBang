package bigBang.module.clientModule.client.userInterface.view;

import bigBang.library.client.HasSelectables;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientProcessToolBar;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;
import bigBang.module.clientModule.shared.Client;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClientSearchOperationView extends View implements ClientSearchOperationViewPresenter.Display {
	
	protected final int SEARCH_PANEL_WIDTH = 400; //minimum and starting width (px)
	//protected final int SEARCH_PREVIEW_PANEL_WIDTH = 400;
	
	protected ClientSearchPanel searchPanel;
	protected ClientFormView form;
	protected ContactsPreviewList contactsList;
	protected ClientProcessToolBar operationsToolbar;  
	
	public ClientSearchOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		searchPanel = new ClientSearchPanel();
		searchPanel.setSize("100%", "100%");
		wrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		wrapper.setWidgetMinSize(searchPanel, SEARCH_PANEL_WIDTH);

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");
		
		this.contactsList = new ContactsPreviewList();
		this.contactsList.setSize("100%", "100%");
		
		contentWrapper.addEast(contactsList, 250);
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		operationsToolbar = new ClientProcessToolBar();

		
		this.form = new ClientFormView();
		this.form.setSize("100%", "100%");
		
		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
		formWrapper.add(form);
		
		contentWrapper.add(formWrapper);
		
		wrapper.add(contentWrapper);
		
		initWidget(wrapper);
	}
	
	public HasSelectables<?> getClientSearchList() {
		return this.searchPanel;
	}

	public View getInstance() {
		return new ClientSearchOperationView();
	}

	@Override
	public void setClient(Client client) {
		// TODO Auto-generated method stub
		
	}

}
