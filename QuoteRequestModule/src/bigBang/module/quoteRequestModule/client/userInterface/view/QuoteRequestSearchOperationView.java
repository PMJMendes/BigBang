package bigBang.module.quoteRequestModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.SecuredObject;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestForm;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestProcessToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestSearchPanel;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSearchOperationViewPresenter;

public class QuoteRequestSearchOperationView extends View implements QuoteRequestSearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX 
	
	protected QuoteRequestSearchPanel searchPanel;
	protected QuoteRequestForm form;
	protected FilterableList<SecuredObject> securedObjectsList;
	protected FilterableList<Negotiation> negotiationsList;
	
	protected NavigationPanel securedObjectsNavigationPanel;
	protected NavigationPanel negotiationsNavigationPanel;
	protected QuoteRequestProcessToolBar operationsToolbar;
	
	public QuoteRequestSearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		ContactsPreviewList contactsList = new ContactsPreviewList();
		mainWrapper.addEast(contactsList, 250);
		
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		mainWrapper.add(wrapper);
		
		searchPanel = new QuoteRequestSearchPanel();
		wrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");
		
		wrapper.add(contentWrapper);
		
		SplitLayoutPanel listsWrapper = new SplitLayoutPanel();
		listsWrapper.setSize("100%", "100%");
		
		contentWrapper.addSouth(listsWrapper, 300);
		
		securedObjectsList = new FilterableList<SecuredObject>();
		negotiationsList = new FilterableList<Negotiation>();
		
		securedObjectsNavigationPanel = new NavigationPanel("Objectos Seguros");
		securedObjectsNavigationPanel.setSize("100%", "100%");
		securedObjectsNavigationPanel.setHomeWidget(securedObjectsList);
		
		negotiationsNavigationPanel = new NavigationPanel("Negociações");
		negotiationsNavigationPanel.setSize("100%", "100%");
		negotiationsNavigationPanel.setHomeWidget(negotiationsList);
		
		listsWrapper.addWest(securedObjectsNavigationPanel, 500);
		listsWrapper.add(negotiationsNavigationPanel);
		
		operationsToolbar = new QuoteRequestProcessToolBar();
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
				
		form = new QuoteRequestForm();
		formWrapper.add(form);
		
		contentWrapper.add(formWrapper);
		
		initWidget(mainWrapper);
	}

}