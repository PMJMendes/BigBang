package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.DocumentsPreviewList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.GeneralSystemModule;
import bigBang.module.generalSystemModule.client.userInterface.InsuranceAgencyList;
import bigBang.module.generalSystemModule.client.userInterface.InsuranceAgencyListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.InsuranceAgencyManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.InsuranceAgencyManagementOperationViewPresenter.Action;
import bigBang.module.generalSystemModule.shared.ModuleConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InsuranceAgencyManagementOperationView extends View implements InsuranceAgencyManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private InsuranceAgencyList insuranceAgencyList;
	private InsuranceAgencyForm insuranceAgencyForm;
	private ContactsPreviewList contactsPreviewList;
	protected DocumentsPreviewList documentsPreviewList;
	protected ActionInvokedEventHandler<InsuranceAgencyManagementOperationViewPresenter.Action> actionHandler;
	protected BigBangOperationsToolBar toolbar;

	public InsuranceAgencyManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		insuranceAgencyList = new InsuranceAgencyList();
		insuranceAgencyList.getNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuranceAgencyManagementOperationViewPresenter.Action>(Action.NEW));
			}
		});	
		insuranceAgencyList.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuranceAgencyManagementOperationViewPresenter.Action>(Action.REFRESH));
			}
		});

		insuranceAgencyList.setSize("100%", "100%");
		wrapper.addWest(insuranceAgencyList, LIST_WIDTH);
		wrapper.setWidgetMinSize(insuranceAgencyList, LIST_WIDTH);

		insuranceAgencyForm = new InsuranceAgencyForm();

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();

		contactsPreviewList = new ContactsPreviewList();
		contactsPreviewList.setSize("100%", "100%");
		contactsPreviewList.setReadOnly(true);
		
		documentsPreviewList = new DocumentsPreviewList();
		documentsPreviewList.setSize("100%", "100%");
		documentsPreviewList.setReadOnly(true);

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		this.toolbar = new BigBangOperationsToolBar() {

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuranceAgencyManagementOperationViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuranceAgencyManagementOperationViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuranceAgencyManagementOperationViewPresenter.Action>(Action.CANCEL_EDIT));
			}
		};
		toolbar.hideAll();
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.ADMIN, true);
		toolbar.addItem(SUB_MENU.ADMIN, new MenuItem("Apagar", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuranceAgencyManagementOperationViewPresenter.Action>(Action.DELETE));
			}
		}));

		formWrapper.add(this.toolbar);
		formWrapper.setCellHeight(this.toolbar, "21px");

		formWrapper.add(insuranceAgencyForm);
		formWrapper.setCellHeight(insuranceAgencyForm, "100%");
		
		insuranceAgencyForm.addValueChangeHandler(new ValueChangeHandler<InsuranceAgency>() {
			@Override
			public void onValueChange(ValueChangeEvent<InsuranceAgency> event) {
				InsuranceAgency agency = event.getValue();
				if(agency != null){
					contactsPreviewList.setContactProcessAndOperationAndOwner(GeneralSystemModule.processId, ModuleConstants.OpTypeIDs.ManageCompanies, agency.id);
				}
				contactsPreviewList.setReadOnly(agency == null); //TODO FJVC
			}
		});

		VerticalPanel sideWrapper = new VerticalPanel();
		sideWrapper.setSize("100%", "100%");
		sideWrapper.add(contactsPreviewList);
		sideWrapper.add(documentsPreviewList);
		sideWrapper.setCellHeight(contactsPreviewList, "50%");
		sideWrapper.setCellHeight(documentsPreviewList, "50%");
		
		contentWrapper.addEast(sideWrapper, 250);
		contentWrapper.add(formWrapper);

		wrapper.add(contentWrapper);
	}

	@Override
	public HasValueSelectables<InsuranceAgency> getList() {
		return (HasValueSelectables<InsuranceAgency>)this.insuranceAgencyList;
	}

	@Override
	public HasEditableValue<InsuranceAgency> getForm() {
		return this.insuranceAgencyForm;
	}

	@Override
	public void prepareNewInsuranceAgency() {
		for(ValueSelectable<InsuranceAgency> s : this.insuranceAgencyList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		InsuranceAgencyListEntry entry = new InsuranceAgencyListEntry(new InsuranceAgency());
		this.insuranceAgencyList.add(entry);
		this.insuranceAgencyList.getScrollable().scrollToBottom();
		entry.setSelected(true, true);
	}

	@Override
	public void removeNewInsuranceAgencyPreparation(){
		for(ValueSelectable<InsuranceAgency> s : this.insuranceAgencyList){
			if(s.getValue().id == null){
				this.insuranceAgencyList.remove(s);
				break;
			}
		}
	}

	@Override
	public boolean isFormValid() {
		return this.insuranceAgencyForm.validate();
	}

	@Override
	public void lockForm(boolean lock) {
		this.insuranceAgencyForm.lock(lock);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		((Button)this.insuranceAgencyList.getNewButton()).setEnabled(!readOnly);
		this.insuranceAgencyForm.setReadOnly(readOnly);
	}

	@Override
	public void clear(){
		this.contactsPreviewList.clear();
		this.insuranceAgencyForm.clearInfo();
		this.insuranceAgencyList.clear();
		this.insuranceAgencyList.clearFilters();
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

}
