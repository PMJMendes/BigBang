package bigBang.library.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.dataAccess.HistoryDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SortOrder;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.shared.HistorySearchParameter;
import bigBang.library.shared.HistorySortParameter;

public class HistoryList extends FilterableList<HistoryItemStub> implements HistoryDataBrokerClient {

	protected static class Entry extends ListEntry<HistoryItemStub>{

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
	
	protected int dataVersion;
	protected String ownerId;
	protected HistoryBroker broker;
	
	public HistoryList(){
		this.broker = ((HistoryBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY));
		this.showFilterField(false);
		this.showSearchField(true);
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		setOwner(ownerId);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		discardOwner();
	}
	
	public void setOwner(String ownerId) {
		discardOwner();
		if(ownerId == null) {return;}
		if(ownerId != null){
//			this.broker.registerClient(this, ownerId);
			
			HistorySearchParameter parameter = new HistorySearchParameter();
			parameter.dataObjectId = ownerId;
			
			HistorySearchParameter[] parameters = new HistorySearchParameter[]{
					parameter
			};
			
			HistorySortParameter sort = new HistorySortParameter(HistorySortParameter.SortableField.TIMESTAMP, SortOrder.DESC);
			HistorySortParameter[] sorts = new HistorySortParameter[]{
					sort
			};
			
			this.broker.getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<HistoryItemStub>>() {

				@Override
				public void onResponse(Search<HistoryItemStub> response) {
					HistoryList.this.clear();
					broker.getSearchBroker().disposeSearch(response.getWorkspaceId());
					for(HistoryItemStub s : response.getResults()) {
						addEntry(s);
 					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					ListEntry<HistoryItemStub> entry = new ListEntry<HistoryItemStub>(null);
					entry.setText("Não foi possível obter o histórico");
					entry.setSelectable(false);
					add(entry);
				}
			});
		}
		this.ownerId = ownerId;
	}

	public void discardOwner(){
		if(ownerId != null){
//			this.broker.unregisterClient(this, this.ownerId);
		}
	}
	
	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.HISTORY)){
			this.dataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.HISTORY)){
			return this.dataVersion;
		}
		return -1;
	}
	
	protected void addEntry(HistoryItemStub item) {
		add(new Entry(item));
	}

	@Override
	public void setHistoryItems(String processId, HistoryItem[] items) {
		this.clear();
		for(int i = 0; i < items.length; i++) {
			addEntry(items[i]);
		}
	}

	@Override
	public void addHistoryItem(String processId, HistoryItem item) {
		addEntry(item);
	}

	@Override
	public void updateHistoryItem(String processId, HistoryItem item) {
		for(ValueSelectable<HistoryItemStub> s : this) {
			if(s.getValue().id.equalsIgnoreCase(item.id)){
				s.setValue(item);
				break;
			}
		}
	}

	@Override
	public void removeHistoryItem(String objectId, HistoryItem item) {
		for(ValueSelectable<HistoryItemStub> s : this) {
			if(s.getValue().id.equalsIgnoreCase(item.id)){
				remove(s);
				break;
			}
		}
	}
	
	@Override
	public void refreshHistory() {
//		broker.requireDataRefresh(this.ownerId);
		this.setOwner(this.ownerId);
	}
	
}