package bigBang.library.client.userInterface.view;

import java.util.Collection;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.dataAccess.HistoryDataBrokerClient;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.UndoOperationsToolbar;
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter.Action;
import bigBang.library.shared.HistorySearchParameter;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class UndoOperationView extends View implements UndoOperationViewPresenter.Display {

	public class UndoItemList extends SearchPanel<HistoryItemStub>  implements HistoryDataBrokerClient {

		protected String currentObjectId;
		protected int dataVersion;
		protected String itemIdToSelect = null;

		public UndoItemList(){
			super(((HistoryBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY)).getSearchBroker());
			ListHeader header = new ListHeader();
			header.setText("Histórico");
			setHeaderWidget(header);
		}

		@Override
		public void doSearch() {
			if(currentObjectId == null)
				throw new RuntimeException("The process id is not defined");
			HistorySearchParameter parameter = new HistorySearchParameter();
			parameter.processId = this.currentObjectId;
			doSearch(new HistorySearchParameter[]{parameter}, new SortParameter[0]);
		}

		@Override
		public void onResults(Collection<HistoryItemStub> results) {
			for(HistoryItemStub s : results) {
				UndoItemListEntry entry = new UndoItemListEntry(s);
				add(entry);
				if(this.itemIdToSelect != null && itemIdToSelect.equalsIgnoreCase(s.id)){
					entry.setSelected(true, true);
					this.itemIdToSelect = null;
				}				
			}
		}

		public void setObjectId(String id){
			this.currentObjectId = id;
//			HistoryBroker historyBroker = ((HistoryBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY));
//			historyBroker.unregisterClient(this);
//			historyBroker.registerClient(this, id); TODO
		}

		@Override
		public void setDataVersionNumber(String dataElementId, int number) {
			if(!dataElementId.equalsIgnoreCase(this.currentObjectId))
				throw new RuntimeException("A data version for a wrong entity was received");
			this.dataVersion = number;
		}

		@Override
		public int getDataVersion(String dataElementId) {
			if(!dataElementId.equalsIgnoreCase(this.currentObjectId))
				throw new RuntimeException("A data version for a wrong entity was requested");
			return this.dataVersion;
		}

		@Override
		public void setHistoryItems(String processId, HistoryItem[] items) {}

		@Override
		public void addHistoryItem(String processId, HistoryItem item) {}

		@Override
		public void updateHistoryItem(String processId, HistoryItem item) {
			if(!processId.equalsIgnoreCase(this.currentObjectId))
				throw new RuntimeException("A reference was made to a wrong entity type");
			for(ValueSelectable<HistoryItemStub> i : this){
				if(i.getValue().id.equalsIgnoreCase(item.id)){
					i.setValue(item);
					break;
				}
			}
		}

		@Override
		public void removeHistoryItem(String processId, HistoryItem item) {
			if(!processId.equalsIgnoreCase(this.currentObjectId))
				throw new RuntimeException("A reference was made to a wrong entity type");
			for(ValueSelectable<HistoryItemStub> i : this){
				if(i.getValue().id.equalsIgnoreCase(item.id)){
					remove(i);
					break;
				}
			}
		}

		public void selectItem(String id){
//			if(id == null) {
//				return;
//			}
//			if(workspaceId == null) {
//				this.itemIdToSelect = id;
//				return;
//			}
//			boolean found = false;
//			boolean searchFinished = !hasResultsLeft();
//			while(!found && !searchFinished){
//				for(ListEntry<HistoryItemStub> i : this){
//					if(i.getValue().id.equalsIgnoreCase(id)){
//						i.setSelected(true, true);
//						found = true;
//						break;
//					}
//				}
//			} TODO
		}
	}

	public class UndoItemListEntry extends ListEntry<HistoryItemStub> {

		public UndoItemListEntry(HistoryItemStub value) {
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			HistoryItemStub value = (HistoryItemStub) info;
			setTitle(value.opName);
			setText(value.username + " (" + value.timeStamp.substring(0, 16) + ")");
			setHeight("40px");
		};

	}

	protected enum Status {
		IDLE,
		PENDING,
		READY
	}

	private static final int LIST_WIDTH = 400; //PX 
	private UndoItemList list;
	private UndoForm form;
	protected UndoOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected Status status;

	public UndoOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		this.list = new UndoItemList();
		wrapper.addWest(list, LIST_WIDTH);

		this.form = new UndoForm();

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		this.toolbar = new UndoOperationsToolbar() {

			@Override
			public void onUndo() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<UndoOperationViewPresenter.Action>(Action.REVERT_OPERATION));
			}

			@Override
			public void onNavigateToAuxiliaryProcess() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<UndoOperationViewPresenter.Action>(Action.NAVIGATE_TO_AUXILIARY_PROCESS));
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
		if(objectId == null){
			this.list.clear();
		}else{
			this.list.doSearch();
		}
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getUndoItemList() {
		return list;
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
