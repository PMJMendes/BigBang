package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.InsuredObject;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.DocumentsPreviewList;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyOperationsToolBar;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter;

public class InsurancePolicySearchOperationView extends View implements InsurancePolicySearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	
	protected InsurancePolicySearchPanel searchPanel;
	protected InsurancePolicyForm form;
	protected ContactsPreviewList contactsList;
	protected DocumentsPreviewList documentsList;
	protected List<InsuredObject> securedObjectsList;
	protected InsurancePolicyOperationsToolBar operationsToolBar;

	public InsurancePolicySearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		searchPanel = new InsurancePolicySearchPanel();
		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");
		
		SplitLayoutPanel listsWrapper = new SplitLayoutPanel();
		listsWrapper.setSize("100%", "100%");
		//contentWrapper.addEast(listsWrapper, 250);
		
		contactsList = new ContactsPreviewList();
		contactsList.setSize("100%", "100%");		
		
		documentsList = new DocumentsPreviewList();
		documentsList.setSize("100%", "100%");
		
		StackPanel stack = new StackPanel();
		stack.setSize("100%", "100%");
		stack.add(contactsList, "Contactos");
		stack.add(documentsList, "Documentos");
		stack.add(new SimplePanel(), "teste");
		stack.add(new SimplePanel(), "teste");
		stack.add(new SimplePanel(), "teste");
		stack.add(new SimplePanel(), "teste");
		stack.add(new SimplePanel(), "teste");
		stack.add(new SimplePanel(), "teste");
		stack.add(new SimplePanel(), "teste");
		stack.add(new SimplePanel(), "teste");
		stack.add(new SimplePanel(), "teste");
		
		contentWrapper.addEast(stack, 250);
		
//		listsWrapper.addNorth(contactsList, 250);
//		listsWrapper.add(documentsList);		
		
		final VerticalPanel toolBarFormContainer = new VerticalPanel();
		toolBarFormContainer.setSize("100%", "100%");
		
		operationsToolBar = new InsurancePolicyOperationsToolBar();

		operationsToolBar.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					toolBarFormContainer.setCellHeight(operationsToolBar, "21px");
				}
			}
		});
		
		SplitLayoutPanel formWrapper = new SplitLayoutPanel();
		formWrapper.setSize("100%", "100%");
				
		securedObjectsList = new List<InsuredObject>();
		securedObjectsList.setHeaderWidget(new ListHeader("Objectos Seguros"));
		
		form = new InsurancePolicyForm();
		form.setSize("100%", "100%");
		
		formWrapper.addSouth(this.securedObjectsList, 300);
		formWrapper.add(form);
		
		toolBarFormContainer.add(operationsToolBar);
		toolBarFormContainer.add(formWrapper);
		
		contentWrapper.add(toolBarFormContainer);
		
		mainWrapper.add(contentWrapper);
		
		initWidget(mainWrapper);
	}
	
}
