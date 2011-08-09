package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.Mediator;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.MediatorList;
import bigBang.module.generalSystemModule.client.userInterface.MediatorListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.MediatorManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.MediatorManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MediatorManagementOperationView extends View implements MediatorManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private MediatorList mediatorList;
	private MediatorForm mediatorForm;
	protected BigBangOperationsToolBar toolbar;
	protected ActionInvokedEventHandler<MediatorManagementOperationViewPresenter.Action> actionHandler;

	public MediatorManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		mediatorList = new MediatorList();
		mediatorList.getNewButton().addClickHandler(new ClickHandler() {

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

		this.toolbar = new BigBangOperationsToolBar() {

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
		};
		toolbar.hideAll();
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.ADMIN, true);
		toolbar.addItem(SUB_MENU.ADMIN, new MenuItem("Apagar", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MediatorManagementOperationViewPresenter.Action>(Action.DELETE));
			}
		}));

		formWrapper.add(toolbar);
		formWrapper.setCellHeight(toolbar, "21px");

		mediatorForm = new MediatorForm();
		formWrapper.add(mediatorForm);



		wrapper.add(formWrapper);

		initWidget(wrapper);
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
	public void prepareNewMediator() {
		for(ValueSelectable<Mediator> s : this.mediatorList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		MediatorListEntry entry = new MediatorListEntry(new Mediator());
		this.mediatorList.add(entry);
		this.mediatorList.getScrollable().scrollToBottom();
		entry.setSelected(true, true);
	}

	@Override
	public void removeNewMediatorPreparation(){
		for(ValueSelectable<Mediator> s : this.mediatorList){
			if(s.getValue().id == null){
				this.mediatorList.remove(s);
				break;
			}
		}
	}

	@Override
	public boolean isFormValid() {
		return this.mediatorForm.validate();
	}

	@Override
	public void lockForm(boolean lock) {
		this.mediatorForm.setReadOnly(true);
		this.mediatorForm.lock(lock);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		((Button)this.mediatorList.getNewButton()).setEnabled(!readOnly);
		this.mediatorForm.setReadOnly(readOnly);
	}

	@Override
	public void clear(){
		this.mediatorForm.clearInfo();
		this.mediatorList.clear();
		this.mediatorList.clearFilters();
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
