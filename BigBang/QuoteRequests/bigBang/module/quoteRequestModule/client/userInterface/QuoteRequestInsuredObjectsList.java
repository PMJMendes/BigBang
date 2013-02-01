package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;

public class QuoteRequestInsuredObjectsList extends FilterableList<QuoteRequestObjectStub> {

	protected class Entry extends ListEntry<QuoteRequestObjectStub>{

		public Entry(QuoteRequestObjectStub value) {
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			QuoteRequestObjectStub o = (QuoteRequestObjectStub) info;
			setTitle(o.unitIdentification);
		};
	}

	protected QuoteRequestBroker quoteRequestBroker;
	protected QuoteRequestObjectDataBroker quoteRequestObjectsBroker;
	protected QuoteRequestObjectDataBrokerClient objectBrokerClient;

	protected String ownerId;

	public QuoteRequestInsuredObjectsList(){
		this.quoteRequestBroker = (QuoteRequestBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
		this.quoteRequestObjectsBroker = (QuoteRequestObjectDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT);
		this.objectBrokerClient = getObjectBrokerClient();
		this.quoteRequestObjectsBroker.registerClient(this.objectBrokerClient);
		showFilterField(false);
	}

	public void setOwner(String ownerId){
		if(ownerId == null) {
			clear();
		}else{
			if(!quoteRequestBroker.isTemp(ownerId) || !ownerId.equalsIgnoreCase(this.ownerId)){
				this.quoteRequestObjectsBroker.getProcessQuoteRequestObjects(ownerId, new ResponseHandler<Collection<QuoteRequestObjectStub>>() {

					@Override
					public void onResponse(Collection<QuoteRequestObjectStub> response) {
						clear();
						for(QuoteRequestObjectStub o : response){
							addEntry(o);
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}
		}
		this.ownerId = ownerId;
	}

	protected void addEntry(QuoteRequestObjectStub object){
		this.add(new Entry(object));
	}

	@Override
	protected void onAttach() {
		clearSelection();
		super.onAttach();
	}	

	protected QuoteRequestObjectDataBrokerClient getObjectBrokerClient() {
		return new QuoteRequestObjectDataBrokerClient() {

			protected int version = 0;

			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				this.version = number;
			}

			@Override
			public int getDataVersion(String dataElementId) {
				return this.version;
			}

			@Override
			public void updateQuoteRequestObject(QuoteRequestObject object) {
				String ownerId = quoteRequestBroker.getFinalMapping(QuoteRequestInsuredObjectsList.this.ownerId);
				String objectOwnerId = quoteRequestBroker.getFinalMapping(object.ownerId);
				if(ownerId != null && objectOwnerId.equalsIgnoreCase(ownerId)) {
					for(ValueSelectable<QuoteRequestObjectStub> entry : QuoteRequestInsuredObjectsList.this) {
						if(entry.getValue().id.equalsIgnoreCase(object.id)) {
							entry.setValue(object);
							break;
						}
					}
				}
			}

			@Override
			public void removeQuoteRequestObject(String id) {
				for(ValueSelectable<QuoteRequestObjectStub> entry : QuoteRequestInsuredObjectsList.this) {
					if(entry.getValue().id.equalsIgnoreCase(id)) {
						remove(entry);
						break;
					}
				}
			}

			@Override
			public void remapItemId(String newId, String oldId) {
				return;
			}

			@Override
			public void addQuoteRequestObject(QuoteRequestObject object) {
				String ownerId = quoteRequestBroker.getFinalMapping(QuoteRequestInsuredObjectsList.this.ownerId);
				String objectOwnerId = quoteRequestBroker.getFinalMapping(object.ownerId);
				if(ownerId != null && objectOwnerId.equalsIgnoreCase(ownerId)) {
					addEntry(object);
				}
			}
		};
	}

}
