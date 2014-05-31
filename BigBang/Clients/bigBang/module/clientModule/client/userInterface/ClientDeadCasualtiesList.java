package bigBang.module.clientModule.client.userInterface;

import java.util.Collection;

import com.google.gwt.event.logical.shared.AttachEvent;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.dataAccess.CasualtyDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.module.casualtyModule.client.userInterface.CasualtySearchPanelListEntry;

public class ClientDeadCasualtiesList extends FilterableList<CasualtyStub> implements CasualtyDataBrokerClient {
	public static class Entry extends CasualtySearchPanelListEntry {

		public Entry(CasualtyStub quoteRequest) {
			super(quoteRequest);
			setLeftWidget(this.openImage);
		}
	}

	protected CasualtyDataBroker broker;
	protected String ownerId;
	protected int dataVersion;

	public ClientDeadCasualtiesList(){
		this.showFilterField(false);
		this.showSearchField(true);

		broker = (CasualtyDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CASUALTY);

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
			broker.getDeadCasualtiesForClient(ownerId, new ResponseHandler<Collection<CasualtyStub>>() {

				@Override
				public void onResponse(Collection<CasualtyStub> response) {
					clear();
					for(CasualtyStub s : response){
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

	public void addEntry(CasualtyStub policy) {
		add(new Entry(policy));
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)) {
			dataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)) {
			return dataVersion;
		}
		return -1;
	}

	@Override
	public void addCasualty(Casualty casualty) {
		if(this.ownerId != null){
			if(casualty.clientId.equalsIgnoreCase(this.ownerId)){
				this.addEntry(casualty);
			}
		}
	}

	@Override
	public void updateCasualty(Casualty Casualty) {
		for(ValueSelectable<CasualtyStub> s : this){
			if(s.getValue().id.equalsIgnoreCase(Casualty.id)){
				s.setValue(Casualty);
				break;
			}
		}
	}

	@Override
	public void removeCasualty(String casualtyId) {
		for(ValueSelectable<CasualtyStub> s : this) {
			if(s.getValue().id.equalsIgnoreCase(casualtyId)){
				remove(s);
				break;
			}
		}
	}

}
