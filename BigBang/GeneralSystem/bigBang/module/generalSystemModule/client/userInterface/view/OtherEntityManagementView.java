package bigBang.module.generalSystemModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.OtherEntity;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.OtherEntityChildrenPanel;
import bigBang.module.generalSystemModule.client.userInterface.OtherEntityList;
import bigBang.module.generalSystemModule.client.userInterface.OtherEntityListEntry;
import bigBang.module.generalSystemModule.client.userInterface.StandardGeneralSystemOperationsToolbar;
import bigBang.module.generalSystemModule.client.userInterface.form.OtherEntityForm;
import bigBang.module.generalSystemModule.client.userInterface.presenter.OtherEntityManagementViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.OtherEntityManagementViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OtherEntityManagementView extends View implements OtherEntityManagementViewPresenter.Display{

	private OtherEntityList list;
	private OtherEntityForm form;
	protected ToolButton newButton;
	protected ActionInvokedEventHandler<OtherEntityManagementViewPresenter.Action> actionHandler;
	protected StandardGeneralSystemOperationsToolbar toolbar;
	protected OtherEntityChildrenPanel childrenPanel;
	
	public OtherEntityManagementView() {
		
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		list = new OtherEntityList();
		list.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<OtherEntityManagementViewPresenter.Action>(Action.REFRESH));
			}
		});	
		this.newButton = (ToolButton) list.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<OtherEntityManagementViewPresenter.Action>(Action.NEW));
			}
		});

		list.setSize("100%", "100%");
		wrapper.addWest(list, 400);
		wrapper.setWidgetMinSize(list, 400);

		form = new OtherEntityForm();

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		this.toolbar = new StandardGeneralSystemOperationsToolbar() {

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<OtherEntityManagementViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<OtherEntityManagementViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<OtherEntityManagementViewPresenter.Action>(Action.CANCEL_EDIT));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<OtherEntityManagementViewPresenter.Action>(Action.DELETE));
			}
		};

		formWrapper.add(this.toolbar);
		formWrapper.setCellHeight(this.toolbar, "21px");

		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");
		
		this.childrenPanel = new  OtherEntityChildrenPanel();
		this.childrenPanel.setSize("100%", "100%");
		
		contentWrapper.addEast(childrenPanel, 250);
		contentWrapper.add(formWrapper);

		wrapper.add(contentWrapper);
		
		this.form.addValueChangeHandler(new ValueChangeHandler<OtherEntity>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<OtherEntity> event) {
				childrenPanel.setOwner(event.getValue());
			}
		});
		
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValueSelectables<OtherEntity> getList() {
		return list;
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
	public HasEditableValue<OtherEntity> getForm() {
		return form;
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
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

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
		
	}

	@Override
	public void prepareNewOtherEntity(OtherEntity newEntity) {
		for(ValueSelectable<OtherEntity> s : this.list){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		OtherEntityListEntry entry = new OtherEntityListEntry(newEntity);
		this.list.add(0, entry);
		this.list.getScrollable().scrollToTop();
		entry.setSelected(true, false);

	}

	@Override
	public void removeFromList(ValueSelectable<OtherEntity> selected) {
		this.list.remove(selected);
	}

}
