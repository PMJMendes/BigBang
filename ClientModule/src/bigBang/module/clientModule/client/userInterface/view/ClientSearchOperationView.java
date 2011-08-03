package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.client.types.Client;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.PopupBar;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientProcessToolBar;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientSearchOperationView extends View implements ClientSearchOperationViewPresenter.Display {
	
	protected final int SEARCH_PANEL_WIDTH = 400; //minimum and starting width (px)
	//protected final int SEARCH_PREVIEW_PANEL_WIDTH = 400;
	
	protected ClientSearchPanel searchPanel;
	protected ClientFormView form;
	protected ContactsPreviewList contactsList;
	protected ClientProcessToolBar operationsToolbar; 
	protected PopupBar childProcessesBar;
	
	public ClientSearchOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		searchPanel = new ClientSearchPanel();
		searchPanel.setSize("100%", "100%");
		wrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		wrapper.setWidgetMinSize(searchPanel, SEARCH_PANEL_WIDTH);

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");
		
		VerticalPanel sideBarWrapper = new VerticalPanel();
		sideBarWrapper.setSize("100%", "100%");

		this.childProcessesBar = new PopupBar();
		this.childProcessesBar.setSize("100%", "100%");
		
		Widget testContent =  new ClientSearchPanel();
		testContent.setSize("300px", "300px");
		this.childProcessesBar.addItem(new PopupBar.Item("Apólices", testContent));

		Widget testContent2 =  new ClientSearchPanel();
		testContent2.setSize("300px", "300px");
		this.childProcessesBar.addItem(new PopupBar.Item("Sinistros", testContent2));
		
		Widget testContent3 =  new ClientSearchPanel();
		testContent3.setSize("300px", "300px");
		this.childProcessesBar.addItem(new PopupBar.Item("C. Mercado", testContent3));
		
		Widget testContent4 =  new ClientSearchPanel();
		testContent4.setSize("400px", "400px");
		this.childProcessesBar.addItem(new PopupBar.Item("A. Risco", testContent4));
		
		Widget testContent5 =  new ClientSearchPanel();
		testContent5.setSize("300px", "300px");
		this.childProcessesBar.addItem(new PopupBar.Item("Reclamação", testContent5));

		sideBarWrapper.add(childProcessesBar);
		sideBarWrapper.setCellWidth(childProcessesBar, "100%");
		
		this.contactsList = new ContactsPreviewList();
		this.contactsList.setSize("100%", "100%");
		
		sideBarWrapper.add(contactsList);
		sideBarWrapper.setCellHeight(this.contactsList, "100%");
		
		contentWrapper.addEast(sideBarWrapper, 250);
		
		final VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		operationsToolbar = new ClientProcessToolBar();
		
		this.form = new ClientFormView();
		this.form.setSize("100%", "100%");
		
		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
		formWrapper.add(form);
		
		contentWrapper.add(formWrapper);

		wrapper.add(contentWrapper);
		form.lock(true);
		
		initWidget(wrapper);
	}
	
	public HasSelectables<?> getClientSearchList() {
		return this.searchPanel;
	}
	
	@Override
	public void setReadOnly(boolean readonly) {
		form.lock(readonly);
	}


	public View getInstance() {
		return new ClientSearchOperationView();
	}

	@Override
	public void setClient(Client client) {
		this.form.setValue(client);
	}

}
