package bigBang.module.clientModule.client.userInterface;

import java.util.Collection;

import com.google.gwt.event.logical.shared.AttachEvent;

import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestSearchPanel;

public class ClientQuoteRequestsList extends FilterableList<QuoteRequestStub> implements QuoteRequestDataBrokerClient {
	public static class Entry extends QuoteRequestSearchPanel.Entry {

		public Entry(QuoteRequestStub quoteRequest) {
			super(quoteRequest);
			setLeftWidget(statusIcon);
		}
	}

	protected QuoteRequestBroker broker;
	protected String ownerId;
	protected int dataVersion;

	public ClientQuoteRequestsList(){
		this.showFilterField(false);
		this.showSearchField(true);

		broker = (QuoteRequestBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);

		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					setOwner(ownerId);
				}else{
					discardOwner();
				}
			}
		});
	}

	public void setOwner(String ownerId){
		discardOwner();
		if(ownerId != null){
			this.broker.registerClient(this);
			broker.getQuoteRequestsForClient(ownerId, new ResponseHandler<Collection<QuoteRequestStub>>() {

				@Override
				public void onResponse(Collection<QuoteRequestStub> response) {
					for(QuoteRequestStub s : response){
						addEntry(s);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {}
			});
		}
		this.ownerId = ownerId;
	}

	public void discardOwner(){
		this.clear();
		if(ownerId != null) {
			broker.unregisterClient(this);
			this.ownerId = null;
		}
	}

	public void addEntry(QuoteRequestStub policy) {
		add(new Entry(policy));
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST)) {
			dataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST)) {
			return dataVersion;
		}
		return -1;
	}

	@Override
	public void addQuoteRequest(QuoteRequest policy) {
		if(this.ownerId != null){
			if(policy.clientId.equalsIgnoreCase(this.ownerId)){
				this.addEntry(policy);
			}
		}
	}

	@Override
	public void updateQuoteRequest(QuoteRequest policy) {
		for(ValueSelectable<QuoteRequestStub> s : this){
			if(s.getValue().id.equalsIgnoreCase(policy.id)){
				s.setValue(policy);
				break;
			}
		}
	}

	@Override
	public void removeQuoteRequest(String requestId) {
		for(ValueSelectable<QuoteRequestStub> s : this) {
			if(s.getValue().id.equalsIgnoreCase(requestId)){
				remove(s);
				break;
			}
		}
	}

	@Override
	public void remapItemId(String oldId, String newId) {
		return;
	}
}
