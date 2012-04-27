package bigBang.module.generalSystemModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.shared.Mediator;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.MediatorChildrenPanel;
import bigBang.module.generalSystemModule.client.userInterface.MediatorList;
import bigBang.module.generalSystemModule.client.userInterface.MediatorListEntry;
import bigBang.module.generalSystemModule.client.userInterface.MediatorOperationsToolbar;
import bigBang.module.generalSystemModule.client.userInterface.presenter.MediatorManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.MediatorManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MediatorManagementOperationView extends View implements MediatorManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private MediatorList mediatorList;
	private MediatorForm mediatorForm;
	protected MediatorOperationsToolbar toolbar;
	protected ToolButton newButton;
	protected ActionInvokedEventHandler<MediatorManagementOperationViewPresenter.Action> actionHandler;
	protected MediatorChildrenPanel childrenPanel;

	public MediatorManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		mediatorList = new MediatorList();
		this.newButton = (ToolButton)mediatorList.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MediatorManagementOperationViewPresenter.Action>(Action.NEW));
			}
		});
		mediatorList.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MediatorManagementOperationViewPresenter.Action>(Action.REFRESH));
			}
		});

		mediatorList.setSize("100%", "100%");
		wrapper.addWest(mediatorList, LIST_WIDTH);
		wrapper.setWidgetMinSize(mediatorList, LIST_WIDTH);

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		this.toolbar = new MediatorOperationsToolbar() {

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MediatorManagementOperationViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MediatorManagementOperationViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MediatorManagementOperationViewPresenter.Action>(Action.CANCEL_EDIT));
			}
			
			@Override
			public void onDelete(){
				actionHandler.onActionInvoked(new ActionInvokedEvent<MediatorManagementOperationViewPresenter.Action>(Action.DELETE));
			}
			
		};

		formWrapper.add(toolbar);
		formWrapper.setCellHeight(toolbar, "21px");

		this.childrenPanel = new MediatorChildrenPanel();
		this.childrenPanel.setSize("100%", "100%");
		wrapper.addEast(this.childrenPanel, 250);
		
		mediatorForm = new MediatorForm();
		formWrapper.add(mediatorForm);
		wrapper.add(formWrapper);
		
		mediatorForm.addValueChangeHandler(new ValueChangeHandler<Mediator>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Mediator> event) {
				childrenPanel.setOwner(event.getValue());
			}
		});
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValueSelectables<Mediator> getList() {
		return (HasValueSelectables<Mediator>)this.mediatorList;
	}

	@Override
	public HasEditableValue<Mediator> getForm() {
		return this.mediatorForm;
	}

	@Override
	public void prepareNewMediator(Mediator mediator) {
		for(ValueSelectable<Mediator> s : this.mediatorList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		MediatorListEntry entry = new MediatorListEntry(mediator);
		this.mediatorList.add(0, entry);
		this.mediatorList.getScrollable().scrollToTop();
		entry.setSelected(true, false);
	}
	@Override
	public boolean isFormValid() {
		return this.mediatorForm.validate();
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
	public void removeFromList(ValueSelectable<Mediator> selectable) {
		this.mediatorList.remove(selectable);
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
