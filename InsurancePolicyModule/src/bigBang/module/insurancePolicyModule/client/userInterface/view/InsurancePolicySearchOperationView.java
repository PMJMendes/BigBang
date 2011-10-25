package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.DocumentsPreviewList;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyOperationsToolBar;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter.Action;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InsurancePolicySearchOperationView extends View implements InsurancePolicySearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	
	protected InsurancePolicySearchPanel searchPanel;
	protected InsurancePolicyForm form;
	protected ContactsPreviewList contactsList;
	protected DocumentsPreviewList documentsList;
	protected List<InsuredObject> securedObjectsList;
	protected InsurancePolicyOperationsToolBar operationsToolBar;
	protected ActionInvokedEventHandler<Action> actionHandler;

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

	@Override
	public HasValueSelectables<?> getList() {
		return this.searchPanel;
	}

	@Override
	public HasEditableValue<InsurancePolicy> getForm() {
		return this.form;
	}

	@Override
	public boolean isFormValid() {
		return form.validate();
	}

	@Override
	public void lockForm(boolean lock) {
		form.setReadOnly(lock);
	}

	@Override
	public void clear() {
		searchPanel.clearSelection();
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void prepareNewPolicy() {
		for(ListEntry<InsurancePolicyStub> s : this.searchPanel){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		InsurancePolicySearchPanel.Entry entry = new InsurancePolicySearchPanel.Entry(new InsurancePolicy());
		this.searchPanel.add(0, entry);
		this.searchPanel.getScrollable().scrollToTop();
		entry.setSelected(true, true);
	}

	@Override
	public void removeNewPolicyPreparation() {
		for(ValueSelectable<InsurancePolicyStub> s : this.searchPanel){
			if(s.getValue().id == null){
				this.searchPanel.remove(s);
				break;
			}
		}
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearAllowedPermissions() {
		// TODO Auto-generated method stub
		
	}
	
}
