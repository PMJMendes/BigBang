package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.dataAccess.HistoryDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.SortOrder;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
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
	protected InsurancePolicy owner;
	protected HistoryBroker broker;

	public HistoryList(){
		this.showFilterField(false);
		this.showSearchField(true);

		this.broker = ((HistoryBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY));
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		setOwner(owner);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		discardOwner();
	}

	public void setOwner(InsurancePolicy owner) {
		discardOwner();
		if(owner == null) {return;}
		if(owner != null){
			this.broker.registerClient(this, owner.processId);

			HistorySearchParameter parameter = new HistorySearchParameter();
			parameter.processId = owner.processId;

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
					broker.getSearchBroker().disposeSearch(response.getWorkspaceId());
					for(HistoryItemStub s : response.getResults()) {
						addEntry(s);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
				}
			});
		}
		this.owner = owner;
	}

	public void discardOwner(){
		this.clear();
		if(owner != null){
			this.broker.unregisterClient(this, this.owner.processId);
			this.owner = null;
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
	public void removeHistoryItem(String processId, HistoryItem item) {
		for(ValueSelectable<HistoryItemStub> s : this) {
			if(s.getValue().id.equalsIgnoreCase(item.id)){
				remove(s);
				break;
			}
		}
	}
}
