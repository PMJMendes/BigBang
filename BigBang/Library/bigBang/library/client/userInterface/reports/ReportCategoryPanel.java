package bigBang.library.client.userInterface.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

import bigBang.definitions.shared.ReportItem;
import bigBang.definitions.shared.ReportItem.ItemType;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;

public class ReportCategoryPanel extends FilterableList<ReportItem> {

	protected class Entry extends ListEntry<ReportItem> {

		protected Image icon;

		public Entry(ReportItem item){
			super(item);
			setHeight("40px");
		}

		public <I extends Object> void setInfo(I info) {
			ReportItem item = (ReportItem) info;
			setTitle(item.label);

			if(icon == null) {
				icon = new Image();
				setRightWidget(icon);
			}

			setSelected(isSelected, false);
		};

		@Override
		public void setSelected(boolean selected, boolean fireEvents) {
			super.setSelected(selected, fireEvents);

			Resources r = GWT.create(Resources.class);

			ReportItem item = getValue();

			if(item != null && item.type == ItemType.CATEGORY) {
				icon.setResource(selected ? r.listNextIconWhite() : r.listNextIconBlack());
			}else if(item != null){
				icon.setResource(r.reportIcon());
			}else{
//				icon.setResource(null);
			}
		}
	} 

	public ReportCategoryPanel(){
		showFilterField(false);
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		this.clearSelection();
	}

	public void setReportItems(ReportItem[] items) {
		clear();
		if(items != null) {
			for(ReportItem item : items) {
				addEntry(item);
			}
		}
	}

	protected void addEntry(ReportItem item){
		Entry entry = new Entry(item);
		add(entry);
	}

}
