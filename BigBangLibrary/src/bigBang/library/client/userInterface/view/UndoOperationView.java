package bigBang.library.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.shared.ProcessUndoItem;

import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;


public class UndoOperationView extends View implements UndoOperationViewPresenter.Display {

	private class UndoItemList extends FilterableList<ProcessUndoItem> {
		
		public UndoItemList(){
			ListHeader header = new ListHeader();
			header.setText("Histórico");
			setHeaderWidget(header);
		}
	}
	
	private class UndoItemListEntry extends ListEntry<ProcessUndoItem> {

		public UndoItemListEntry(ProcessUndoItem value) {
			super(value);
			setTitle(value.shortDescription);
			setText(value.username + " (" + value.timeStamp.substring(0, 16) + ")");
			setHeight("40px");
			setMetaData(new String[]{value.shortDescription, value.description});
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
	public void setUndoItems(ProcessUndoItem[] items) {
		this.list.clear();
		for(int i = 0; i < items.length; i++) {
			addItem(items[i]);
		}
	}


	@Override
	public void addItem(ProcessUndoItem item) {
		list.add(new UndoItemListEntry(item));
	}
	
	
	@Override
	public void removeUndoItem(ProcessUndoItem item) {
		for(ValueSelectable<ProcessUndoItem> s : this.list) {
			if(s.getValue().id.equals(item.id)){
				list.remove(s);
				break;
			}
		}
	}

	@Override
	public HasValueSelectables<ProcessUndoItem> getUndoItemList() {
		return list;
	}

	@Override
	public HasValue<ProcessUndoItem> getForm() {
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
