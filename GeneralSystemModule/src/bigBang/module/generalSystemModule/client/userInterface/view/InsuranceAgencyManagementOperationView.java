package bigBang.module.generalSystemModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.DocumentsPreviewList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.InsuranceAgencyList;
import bigBang.module.generalSystemModule.client.userInterface.InsuranceAgencyListEntry;
import bigBang.module.generalSystemModule.client.userInterface.InsuranceAgencyOperationsToolbar;
import bigBang.module.generalSystemModule.client.userInterface.presenter.InsuranceAgencyManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.InsuranceAgencyManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InsuranceAgencyManagementOperationView extends View implements InsuranceAgencyManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private InsuranceAgencyList insuranceAgencyList;
	private InsuranceAgencyForm insuranceAgencyForm;
	private ContactsPreviewList contactsPreviewList;
	protected ToolButton newButton;
	protected DocumentsPreviewList documentsPreviewList;
	protected ActionInvokedEventHandler<InsuranceAgencyManagementOperationViewPresenter.Action> actionHandler;
	protected InsuranceAgencyOperationsToolbar toolbar;

	public InsuranceAgencyManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		insuranceAgencyList = new InsuranceAgencyList();
		insuranceAgencyList.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuranceAgencyManagementOperationViewPresenter.Action>(Action.REFRESH));
			}
		});	
		this.newButton = (ToolButton) insuranceAgencyList.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuranceAgencyManagementOperationViewPresenter.Action>(Action.NEW));
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

		this.toolbar = new InsuranceAgencyOperationsToolbar() {

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

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsuranceAgencyManagementOperationViewPresenter.Action>(Action.DELETE));
			}
		};

		formWrapper.add(this.toolbar);
		formWrapper.setCellHeight(this.toolbar, "21px");

		formWrapper.add(insuranceAgencyForm);
		formWrapper.setCellHeight(insuranceAgencyForm, "100%");
		
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
	protected void initializeView() {
		return;
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
	public void prepareNewInsuranceAgency(InsuranceAgency agency) {
		for(ValueSelectable<InsuranceAgency> s : this.insuranceAgencyList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		InsuranceAgencyListEntry entry = new InsuranceAgencyListEntry(agency);
		this.insuranceAgencyList.add(0, entry);
		this.insuranceAgencyList.getScrollable().scrollToTop();
		entry.setSelected(true, false);
	}

	@Override
	public boolean isFormValid() {
		return this.insuranceAgencyForm.validate();
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

	@Override
	public void removeFromList(ValueSelectable<InsuranceAgency> selectable) {
		this.insuranceAgencyList.remove(selectable);
	}

	@Override
	public void clearAllowedPermissions() {
		this.toolbar.lockAll();
	}

	@Override
	public void allowCreate(boolean allow) {
		this.newButton.setEnabled(allow);
	}

	@Override
	public void allowEdit(boolean allow) {
		this.toolbar.setEditionAvailable(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		this.toolbar.allowDelete(allow);
	}

}
