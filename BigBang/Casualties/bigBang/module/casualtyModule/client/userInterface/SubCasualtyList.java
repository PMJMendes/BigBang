package bigBang.module.casualtyModule.client.userInterface;

import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;

public class SubCasualtyList extends FilterableList<SubCasualtyStub> implements SubCasualtyDataBrokerClient {
	public static class Entry extends ListEntry<SubCasualtyStub> {

		public Entry(SubCasualtyStub value) {
			super(value);
			setHeight("25px");
			titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		}

		public <I extends Object> void setInfo(I info) {
			SubCasualtyStub c = (SubCasualtyStub) info;
			this.setTitle(c.number); //TODO
		};
	}

	protected String ownerId;
	protected SubCasualtyDataBroker broker;
	protected int dataVersion;

	public SubCasualtyList(){
		this.showFilterField(false);
		this.showSearchField(true);
		
		this.broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()) {
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
		}
		this.ownerId = ownerId;
	}

	public void discardOwner(){
		this.clear();
		if(ownerId != null){
			this.broker.unregisterClient(this);
			this.ownerId = null;
		}
	}

	public void addEntry(SubCasualty c){
		this.add(new Entry(c));
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		this.dataVersion = number;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return dataVersion;
	}

	@Override
	public void addSubCasualty(SubCasualty subCasualty) {
		if(this.ownerId != null && subCasualty != null && subCasualty.casualtyId.equalsIgnoreCase(this.ownerId)){
			addEntry(subCasualty);
		}
	}

	@Override
	public void updateSubCasualty(SubCasualty subCasualty) {
		if(this.ownerId != null && subCasualty != null && subCasualty.casualtyId.equalsIgnoreCase(this.ownerId)){
			for(ValueSelectable<SubCasualtyStub> entry : this) {
				if(entry.getValue().id.equalsIgnoreCase(subCasualty.id)) {
					entry.setValue(subCasualty);
					break;
				}
			}
		}
	}

	@Override
	public void removeSubCasualty(String id) {
		if(this.ownerId != null && id != null){
			for(ValueSelectable<SubCasualtyStub> entry : this) {
				if(entry.getValue().id.equalsIgnoreCase(id)) {
					remove(entry);
					break;
				}
			}
		}
	}
}
