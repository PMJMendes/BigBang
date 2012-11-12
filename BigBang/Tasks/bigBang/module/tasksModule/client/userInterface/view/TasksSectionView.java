package bigBang.module.tasksModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.DockItem;
import bigBang.library.client.userInterface.DockPanel;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.tasksModule.client.resources.Resources;
import bigBang.module.tasksModule.client.userInterface.presenter.TasksSectionViewPresenter;
import bigBang.module.tasksModule.client.userInterface.presenter.TasksSectionViewPresenter.Action;
import bigBang.module.tasksModule.client.userInterface.presenter.TasksSectionViewPresenter.SectionOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TasksSectionView extends View implements TasksSectionViewPresenter.Display {

	private ActionInvokedEventHandler<SectionOperation> operationSelectionHandler;
	private DockPanel operationDock;
	private ActionInvokedEventHandler<TasksSectionViewPresenter.Action> actionHandler;
	private SimplePanel operationViewContainer;
	private SimplePanel overlayContainer;
	private PopupPanel popupPanel;

	public TasksSectionView() {
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		this.operationDock = new DockPanel();
		wrapper.add(this.operationDock);
		initializeDock();
		this.operationDock.addValueChangeHandler(new ValueChangeHandler<Object>() {


			@Override
			public void onValueChange(ValueChangeEvent<Object> event) {
				operationSelectionHandler.onActionInvoked(new ActionInvokedEvent<TasksSectionViewPresenter.SectionOperation>((SectionOperation)event.getValue()));
			}
		});

		operationViewContainer = new SimplePanel();
		this.operationViewContainer.setSize("100%", "100%");
		wrapper.add(operationViewContainer);
		wrapper.setCellHeight(operationViewContainer, "100%");

		overlayContainer = new SimplePanel();
	}

	private void initializeDock() {
		Resources r = GWT.create(Resources.class);

		addDockItem("Agenda", r.agendaIcon(), SectionOperation.AGENDA, "100px");
		addDockItem("Organizar Correio", r.mailIcon(), SectionOperation.MAIL_ORGANIZER);

	}

	private void addDockItem(String text, ImageResource icon, final TasksSectionViewPresenter.SectionOperation action, String width) {
		DockItem item = null;
		if(icon == null){
			item = new DockItem(text, MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), action);
		}else{
			item = new DockItem(text, icon, action);
		}
		item.setTitle(text);
		if(width != null){
			item.setWidth(width);
		}
		this.operationDock.addItem(item);		
	}

	protected void addDockItem(String text, ImageResource icon, final TasksSectionViewPresenter.SectionOperation action){
		addDockItem(text, icon, action, null);
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasWidgets getOperationViewContainer() {
		return operationViewContainer;
	}



	@Override
	public void showOverlayViewContainer(boolean show) {
		if(show && this.popupPanel == null){
			this.popupPanel = new PopupPanel(){
				@Override
				protected void onDetach() {
					super.onDetach();
					actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.ON_OVERLAY_CLOSED));
					TasksSectionView.this.popupPanel = null;
				}
			};
			this.popupPanel.add((Widget)this.overlayContainer);
		}

		if(this.popupPanel != null){
			if(show && !this.popupPanel.isAttached()){
				this.popupPanel.center();
			}else if(this.popupPanel.isAttached() && !show){
				this.popupPanel.hidePopup();
				this.popupPanel.remove((Widget)this.overlayContainer);
				this.popupPanel = null;
			}
		}
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<bigBang.module.tasksModule.client.userInterface.presenter.TasksSectionViewPresenter.Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;
	}

	@Override
	public void registerOperationSelectionHandler(
			ActionInvokedEventHandler<SectionOperation> actionInvokedEventHandler) {
		this.operationSelectionHandler = actionInvokedEventHandler;
	}

	@Override
	public void selectOperation(SectionOperation operation) {
		this.operationDock.setValue(operation, false);		
	}

	@Override
	public HasWidgets getOverlayViewContainer() {
		return overlayContainer;
	}

}
