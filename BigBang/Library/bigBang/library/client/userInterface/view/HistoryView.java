package bigBang.library.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.HistoryOperationsToolbar;
import bigBang.library.client.userInterface.HistorySearchPanel;
import bigBang.library.client.userInterface.presenter.HistoryViewPresenter;
import bigBang.library.client.userInterface.presenter.HistoryViewPresenter.Action;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class HistoryView extends View implements HistoryViewPresenter.Display {

	protected enum Status {
		IDLE,
		PENDING,
		READY
	}

	private static final int LIST_WIDTH = 400; //PX 
	private HistorySearchPanel list;
	private HistoryItemForm form;
	protected HistoryOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected Status status;

	public HistoryView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		this.list = new HistorySearchPanel();
		wrapper.addWest(list, LIST_WIDTH);

		this.form = new HistoryItemForm();

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		this.toolbar = new HistoryOperationsToolbar() {

			@Override
			public void onUndo() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<HistoryViewPresenter.Action>(Action.REVERT_OPERATION));
			}

			@Override
			public void onNavigateToAuxiliaryProcess() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<HistoryViewPresenter.Action>(Action.NAVIGATE_TO_AUXILIARY_PROCESS));
			}
		};
		formWrapper.add(toolbar);
		formWrapper.setCellHeight(toolbar, "21px");

		formWrapper.add(this.form);
		formWrapper.setCellHeight(this.form, "100%");

		wrapper.add(formWrapper);

		this.status = Status.IDLE;
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setObjectId(String objectId) {
		this.list.setObjectId(objectId);
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getUndoItemList() {
		return list;
	}

	@Override
	public void refreshList() {
		this.list.doSearch();
	}

	@Override
	public HasValue<HistoryItem> getForm() {
		return form;
	}


	@Override
	public void confirmUndo(final ResponseHandler<Boolean> handler) {
		MessageBox.confirm("", "Tem certeza que pretende reverter esta operação?", new ConfirmationCallback() {

			@Override
			public void onResult(boolean result) {
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void selectItem(String id) {
		this.list.selectItem(id);
	}

	@Override
	public void allowUndo(boolean allow) {
		this.toolbar.allowUndo(allow);
	}

	@Override
	public void allowNavigateToAuxiliaryProcess(boolean allow) {
		this.toolbar.allowNavigateToAuxiliaryProcess(allow);
	}

}
