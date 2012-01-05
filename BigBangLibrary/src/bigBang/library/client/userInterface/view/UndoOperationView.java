package bigBang.library.client.userInterface.view;

import java.util.Collection;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.dataAccess.HistoryDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;

import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter.Action;
import bigBang.library.shared.HistorySearchParameter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class UndoOperationView extends View implements UndoOperationViewPresenter.Display {

	public class UndoItemList extends SearchPanel<HistoryItemStub>  implements HistoryDataBrokerClient {

		protected String currentProcessId;
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
			if(currentProcessId == null)
				throw new RuntimeException("The process id is not defined");
			HistorySearchParameter parameter = new HistorySearchParameter();
			parameter.processId = this.currentProcessId;
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

		public void setProcessId(String id){
			this.currentProcessId = id;
			HistoryBroker historyBroker = ((HistoryBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY));
			historyBroker.registerClient(this, id);
		}

		@Override
		public void setDataVersionNumber(String dataElementId, int number) {
			if(!dataElementId.equalsIgnoreCase(this.currentProcessId))
				throw new RuntimeException("A data version for a wrong entity was received");
			this.dataVersion = number;
		}

		@Override
		public int getDataVersion(String dataElementId) {
			if(!dataElementId.equalsIgnoreCase(this.currentProcessId))
				throw new RuntimeException("A data version for a wrong entity was requested");
			return this.dataVersion;
		}

		@Override
		public void setHistoryItems(String processId, HistoryItem[] items) {}

		@Override
		public void addHistoryItem(String processId, HistoryItem item) {}

		@Override
		public void updateHistoryItem(String processId, HistoryItem item) {
			if(!processId.equalsIgnoreCase(this.currentProcessId))
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
			if(!processId.equalsIgnoreCase(this.currentProcessId))
				throw new RuntimeException("A reference was made to a wrong entity type");
			for(ValueSelectable<HistoryItemStub> i : this){
				if(i.getValue().id.equalsIgnoreCase(item.id)){
					remove(i);
					break;
				}
			}
		}
		
		public void selectItem(String id){
			if(id == null) {
				return;
			}
			if(workspaceId == null) {
				this.itemIdToSelect = id;
				return;
			}
			boolean found = false;
			boolean searchFinished = !hasResultsLeft();
			while(!found && !searchFinished){
				for(ListEntry<HistoryItemStub> i : this){
					if(i.getValue().id.equalsIgnoreCase(id)){
						i.setSelected(true, true);
						found = true;
						break;
					}
				}
			}
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
	protected BigBangOperationsToolBar toolbar;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected Status status;

	public UndoOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		this.list = new UndoItemList();
		wrapper.addWest(list, LIST_WIDTH);

		this.form = new UndoForm();

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		this.toolbar = new BigBangOperationsToolBar() {

			@Override
			public void onSaveRequest() {}

			@Override
			public void onEditRequest() {}

			@Override
			public void onCancelRequest() {}
		};
		toolbar.hideAll();
		toolbar.addItem(new MenuItem("Desfazer Operação", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.REVERT_OPERATION));
			}
		}));
		toolbar.addItem(new MenuItem("Navegar para Processo Auxiliar", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.NAVIGATE_TO_AUXILIARY_PROCESS));
			}
		}));

		formWrapper.add(toolbar);
		formWrapper.setCellHeight(toolbar, "21px");

		formWrapper.add(this.form);
		formWrapper.setCellHeight(this.form, "100%");

		wrapper.add(formWrapper);

		this.status = Status.IDLE;
		
		initWidget(wrapper);
	}

	@Override
	public void setProcessId(String processId) {
		this.list.setProcessId(processId);
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
	public void showConfirmUndo(ConfirmationCallback callback) {
		MessageBox.confirm("Confirmação", "Tem certeza que pretende desfazer esta operação?", callback);
	}

	@Override
	public void refreshList() {
		this.list.doSearch();
	}

	@Override
	public void showErrors(Collection<ResponseError> errors) {
		String message = "A operação não foi desfeita pelas seguintes razões:";
		for(ResponseError e : errors)
			message += "</br>" + e.description;
				MessageBox.alert("Não foi possível desfazer a operação", message);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void clear() {
		this.list.clearSelection();
		this.form.clearInfo();
	}

	@Override
	public void setUndoable(boolean undoable) {
		this.toolbar.setEditionAvailable(undoable);
	}

	@Override
	public void selectItem(String id) {
		this.list.selectItem(id);
	}

}
