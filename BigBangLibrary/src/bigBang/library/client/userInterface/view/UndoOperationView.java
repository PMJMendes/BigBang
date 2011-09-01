package bigBang.library.client.userInterface.view;

import java.util.Collection;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.dataAccess.SortParameter;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;

import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.shared.HistorySearchParameter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;


public class UndoOperationView extends View implements UndoOperationViewPresenter.Display {

	private class UndoItemList extends SearchPanel<HistoryItemStub> {
		
		protected String currentProcessId;
		
		public UndoItemList(){
			super(((HistoryBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY)).getSearchBroker());
			ListHeader header = new ListHeader();
			header.setText("Histórico");
			setHeaderWidget(header);
			
			addAttachHandler(new AttachEvent.Handler() {
				
				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached())
						doSearch();
				}
			});
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
				add(new UndoItemListEntry(s));
			}
		}
		
		public void setProcessId(String id){
			this.currentProcessId = id;
		}
	}
	
	private class UndoItemListEntry extends ListEntry<HistoryItemStub> {

		public UndoItemListEntry(HistoryItemStub value) {
			super(value);
			setTitle(value.opName);
			setText(value.username + " (" + value.timeStamp.substring(0, 16) + ")");
			setHeight("40px");
		}

	}
	
	private static final int LIST_WIDTH = 400; //PX 
	private UndoItemList list;
	private UndoForm form;
	
	public UndoOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		this.list = new UndoItemList();
		wrapper.addWest(list, LIST_WIDTH);
		
		this.form = new UndoForm();
		wrapper.add(form);
		
		initWidget(wrapper);
	}

	@Override
	public void setProcessId(String processId) {
		this.list.setProcessId(processId);
	}


	@Override
	public void addItem(HistoryItemStub item) {
		list.add(new UndoItemListEntry(item));
	}
	
	
	@Override
	public void removeUndoItem(HistoryItemStub item) {
		for(ValueSelectable<HistoryItemStub> s : this.list) {
			if(s.getValue().id.equals(item.id)){
				list.remove(s);
				break;
			}
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
	public HasClickHandlers getUndoButton() {
		return form.getUndoButton();
	}

	@Override
	public void showConfirmUndo(ConfirmationCallback callback) {
		MessageBox.confirm("Confirmação", "Tem certeza que pretende desfazer esta operação?", callback);
	}

}
