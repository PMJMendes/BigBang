package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.casualtyModule.client.userInterface.CasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyChildrenPanel;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyOperationsToolbar;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyViewPresenter.Action;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;

public class SubCasualtyView extends View implements SubCasualtyViewPresenter.Display {

	protected CasualtyForm parentForm;
	protected SubCasualtyForm form;
	protected SubCasualtyOperationsToolbar toolbar;
	protected SubCasualtyChildrenPanel childrenPanel;
	protected ActionInvokedEventHandler<Action> actionHandler;

	public SubCasualtyView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		VerticalPanel parentWrapper = new VerticalPanel();
		parentWrapper.setSize("100%", "100%");
		wrapper.addWest(parentWrapper, 600);

		ListHeader parentHeader = new ListHeader("Sinistro");
		parentWrapper.add(parentHeader);

		parentForm = new CasualtyForm();
		parentForm.setSize("100%", "100%");
		parentForm.setReadOnly(true);
		parentWrapper.add(parentForm);
		parentWrapper.setCellHeight(parentForm, "100%");

		SplitLayoutPanel subCasualtyWrapper = new SplitLayoutPanel();
		subCasualtyWrapper.setSize("100%", "100%");
		wrapper.add(subCasualtyWrapper);

		childrenPanel = new SubCasualtyChildrenPanel();
		subCasualtyWrapper.addEast(childrenPanel, 250);

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		subCasualtyWrapper.add(formWrapper);

		ListHeader formHeader = new ListHeader("Sub-Sinistro");
		formWrapper.add(formHeader);

		this.toolbar = new SubCasualtyOperationsToolbar() {

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyViewPresenter.Action>(Action.CANCEL));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyViewPresenter.Action>(Action.DELETE));
			}

			@Override
			public void onClose() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyViewPresenter.Action>(Action.CLOSE));
			}

			@Override
			public void onRejectClose() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyViewPresenter.Action>(Action.REJECT_CLOSE));
			}

			@Override
			public void onMarkForClosing() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyViewPresenter.Action>(Action.MARK_FOR_CLOSING));
			}

			@Override
			protected void onExternalInfoRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyViewPresenter.Action>(Action.EXTERNAL_REQUEST));
			}

			@Override
			protected void onInsurerInfoRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubCasualtyViewPresenter.Action>(Action.INFO_OR_DOCUMENT_REQUEST));
			}
		};
		formWrapper.add(this.toolbar);

		form = new SubCasualtyForm();
		form.setSize("100%", "100%");
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");

		form.addValueChangeHandler(new ValueChangeHandler<SubCasualty>() {

			@Override
			public void onValueChange(ValueChangeEvent<SubCasualty> event) {
				childrenPanel.setSubCasualty(event.getValue());
			}
		});
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<Casualty> getParentForm() {
		return this.parentForm;
	}

	@Override
	public HasEditableValue<SubCasualty> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		this.toolbar.lockAll();
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void allowEdit(boolean allow) {
		this.toolbar.allowEdit(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		this.toolbar.allowDelete(allow);
	}
	
	@Override
	public void allowMarkForClosing(boolean allow) {
		this.toolbar.allowMarkForClosing(allow);
	}
	
	@Override
	public void allowClose(boolean allow) {
		this.toolbar.allowClose(allow);
	}
	
	@Override
	public void allowRejectClose(boolean allow){
		this.toolbar.allowRejectClose(allow);
	}

	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessesList() {
		return this.childrenPanel.subProcessesList;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return this.childrenPanel.historyList;
	}

	@Override
	public void allowInfoOrDocumentRequest(boolean hasPermission) {
		toolbar.allowInfoOrDocumentRequest(hasPermission);

	}

	@Override
	public void allowInsurerInfoRequest(boolean hasPermission) {
		toolbar.allowInsurerInfoRequest(hasPermission);
	}

	@Override
	public HasValueSelectables<Contact> getContactsList() {
		return childrenPanel.contactsList;
	}

	@Override
	public HasValueSelectables<Document> getDocumentsList() {
		return childrenPanel.documentsList;
	}

	@Override
	public void setReferenceParameters(HasParameters parameterHolder) {
		form.setPanelParameters(parameterHolder);
	}
}
