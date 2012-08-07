package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.dataAccess.HistoryDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.library.shared.HistorySearchParameter;
import bigBang.library.shared.HistorySortParameter;
import bigBang.library.shared.HistorySortParameter.SortableField;

public class HistorySearchPanel extends SearchPanel<HistoryItemStub>  implements HistoryDataBrokerClient {

	public class Entry extends ListEntry<HistoryItemStub> {

		public Entry(HistoryItemStub value) {
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			HistoryItemStub value = (HistoryItemStub) info;
			setTitle(value.opName);
			setText(value.username + " (" + value.timeStamp.substring(0, 16) + ")");
			setHeight("40px");
		};

	}
	
	public static enum Filters {
		AFTER_TIMESTAMP
	}
	
	protected String currentObjectId;
	protected int dataVersion;
	protected String itemIdToSelect = null;
	protected FiltersPanel filtersPanel;
	protected ListHeader header;

	public HistorySearchPanel(){
		super(((HistoryBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY)).getSearchBroker());
		this.header = new ListHeader();
		header.setText("Histórico");
		setHeaderWidget(header);
		
		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
		sortOptions.put(HistorySortParameter.SortableField.TIMESTAMP, "Data");
		
		filtersPanel = new FiltersPanel(sortOptions);
		filtersPanel.addDateField(Filters.AFTER_TIMESTAMP, "Data Após");

		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSearch();
			}
		});

		filtersContainer.clear();
		filtersContainer.add(filtersPanel);
	}

	@Override
	public void doSearch() {
		if(currentObjectId != null){
			HistorySearchParameter parameter = new HistorySearchParameter();
			parameter.dataObjectId = this.currentObjectId;
			Date afterTimestamp = (Date) filtersPanel.getFilterValue(Filters.AFTER_TIMESTAMP);
			parameter.afterTimestamp = afterTimestamp == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(afterTimestamp);
			
			HistorySortParameter sort = new HistorySortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder());

			doSearch(new HistorySearchParameter[]{parameter}, new SortParameter[]{sort});
		}
		itemIdToSelect = null;
	}

	@Override
	public void onResults(Collection<HistoryItemStub> results) {
		for(HistoryItemStub s : results) {
			Entry entry = new Entry(s);
			add(entry);
			if(this.itemIdToSelect != null && itemIdToSelect.equalsIgnoreCase(s.id)){
				entry.setSelected(true, true);
				this.itemIdToSelect = null;
			}				
		}
	}

	public void setObjectId(String id){
		if(id == null){
			currentObjectId = id;
			HistoryBroker historyBroker = ((HistoryBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY));
			historyBroker.unregisterClient(this);
			clear();
		}else if(currentObjectId == null || !id.equalsIgnoreCase(currentObjectId)){
			currentObjectId = id;
			HistoryBroker historyBroker = ((HistoryBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY));
			historyBroker.unregisterClient(this);
			historyBroker.registerClient(this, id);
			doSearch();
		}
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
	public void setHistoryItems(String objectId, HistoryItem[] items) {}

	@Override
	public void addHistoryItem(String objectId, HistoryItem item) {}

	@Override
	public void updateHistoryItem(String objectId, HistoryItem item) {
		if(!objectId.equalsIgnoreCase(this.currentObjectId))
			throw new RuntimeException("A reference was made to a wrong entity type");
		for(ValueSelectable<HistoryItemStub> i : this){
			if(i.getValue().id.equalsIgnoreCase(item.id)){
				i.setValue(item);
				break;
			}
		}
	}

	@Override
	public void removeHistoryItem(String objectId, HistoryItem item) {
		if(!objectId.equalsIgnoreCase(this.currentObjectId))
			throw new RuntimeException("A reference was made to a wrong entity type");
		for(ValueSelectable<HistoryItemStub> i : this){
			if(i.getValue().id.equalsIgnoreCase(item.id)){
				remove(i);
				break;
			}
		}
	}

	public void selectItem(String id){
		for(ListEntry<HistoryItemStub> i : this){
			if(i.getValue().id.equalsIgnoreCase(id)){
				i.setSelected(true, false);
				break;
			}
		}

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
		//			}
	}

	@Override
	public void refreshHistory() {
		Collection<ValueSelectable<HistoryItemStub>> selected = getSelected();
		String id = null;
		if(selected != null){
			for(ValueSelectable<HistoryItemStub> selectable : selected){
				id = selectable.getValue().id;
			}
		}
		this.doSearch();
		if(id != null){
			selectItem(id);
		}
	}

	public ListHeader getHeader() {
		return this.header;
	}

}
