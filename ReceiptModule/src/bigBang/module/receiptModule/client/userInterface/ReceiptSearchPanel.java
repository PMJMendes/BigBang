package bigBang.module.receiptModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import bigBang.definitions.client.dataAccess.ReceiptDataBrokerClient;
import bigBang.definitions.client.dataAccess.ReceiptProcessDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.receiptModule.shared.ReceiptSearchParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter;

public class ReceiptSearchPanel extends SearchPanel<ReceiptStub> implements ReceiptDataBrokerClient {

	public static class Entry extends ListEntry<ReceiptStub> {

		public Entry(ReceiptStub value) {
			super(value);
			setHeight("40px");
		}
		
		public <I extends Object> void setInfo(I info) {
			ReceiptStub r = (ReceiptStub) info;
			setTitle(r.description);
			//TODO
		};
	}
	
	protected static enum Filters {
		//TODO
	}
	
	protected int dataVersion;
	protected FiltersPanel filtersPanel;
	protected Map<String, Receipt> receiptsToUpdate;
	protected Map<String, Void> receiptsToRemove;
	
	public ReceiptSearchPanel() {
		super(((ReceiptProcessDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT)).getSearchBroker());
		receiptsToRemove = new HashMap<String, Void>();
		receiptsToUpdate = new HashMap<String, Receipt>();
		
		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>();
		
		filtersPanel = new FiltersPanel(sortOptions);
		
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
		ReceiptSearchParameter parameter = new ReceiptSearchParameter();
		parameter.freeText = this.getFreeText();
		
		SearchParameter[] parameters = new SearchParameter[] {
				//parameter
		};
		
		ReceiptSortParameter sortParameter = new ReceiptSortParameter(ReceiptSortParameter.SortableField.RELEVANCE, SortOrder.DESC);
		ReceiptSortParameter[] sorts = new ReceiptSortParameter[]{
				//sortParameter
		};
		
		doSearch(parameters, sorts);
	}

	@Override
	public void onResults(Collection<ReceiptStub> results) {
		for(ReceiptStub s : results) {
			if(!receiptsToRemove.containsKey(s.id)){
				if(receiptsToUpdate.containsKey(s.id)){
					s = receiptsToUpdate.get(s.id);
				}
				addEntry(s);
			}
		}
	}
	
	public Entry addEntry(ReceiptStub receipt){
		Entry entry = null;
		if(receipt instanceof ReceiptStub) {
			entry = new Entry(receipt);
			add(entry);
		}
		return entry;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)) {
			this.dataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)) {
			return dataVersion;
		}
		return -1;
	}

	@Override
	public void addReceipt(Receipt receipt) {
		add(0, new Entry(receipt));
	}

	@Override
	public void updateReceipt(Receipt receipt) {
		for(ValueSelectable<ReceiptStub> s : this) {
			ReceiptStub receiptStub = s.getValue();
			if(receipt.id.equalsIgnoreCase(receiptStub.id)) {
				s.setValue(receipt);
				return;
			}
		}
		this.receiptsToUpdate.put(receipt.id, receipt);
	}

	@Override
	public void removeReceipt(String id) {
		for(ValueSelectable<ReceiptStub> s : this) {
			ReceiptStub receiptStub = s.getValue();
			if(id.equalsIgnoreCase(receiptStub.id)) {
				remove(s);
				return;
			}
		}
		this.receiptsToRemove.put(id, null);
	}

}
