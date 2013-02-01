package bigBang.library.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.SubProcessesBroker;
import bigBang.library.client.dataAccess.SubProcessesBrokerClient;

public class SubProcessesList extends FilterableList<BigBangProcess> implements SubProcessesBrokerClient {

	public static class Entry extends ListEntry<BigBangProcess> {

		public Entry(BigBangProcess process){
			super(process);
			setHeight("40px");
		}

		public <I extends Object> void setInfo(I info) {
			BigBangProcess process = (BigBangProcess) info;
			setTitle(process.tag);
			setText(process.dataLabel == null ? "-" : process.dataLabel);
		};
	}

	private SubProcessesBroker broker;
	private String ownerId;
	private int dataVersion;

	public SubProcessesList(){
		this.broker = (SubProcessesBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.PROCESS);
		this.showFilterField(false);
		this.showSearchField(true);
	}

	public void setOwner(String ownerId){
		discardOwner();
		if(ownerId != null){
			this.broker.registerClient(ownerId, this);
			broker.getSubProcesses(ownerId, new ResponseHandler<Collection<BigBangProcess>>() {

				@Override
				public void onResponse(Collection<BigBangProcess> response) {
					clear();
					for(BigBangProcess process : response) {
						addEntry(process);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
			this.ownerId = ownerId;
		}
	}
	
	private void discardOwner(){
		this.clear();
		if(ownerId != null) {
			broker.unregisterClient(ownerId, this);
			this.ownerId = null;
		}
	}

	private void addEntry(BigBangProcess process){
		add(new Entry(process));
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
	public void setSubProcesses(Collection<BigBangProcess> subProcesses) {
		clear();
		for(BigBangProcess process : subProcesses){
			if(process.ownerDataId.equalsIgnoreCase(this.ownerId)){
				addEntry(process);
			}
		}
	}

	@Override
	public void addSubProcess(BigBangProcess subProcess) {
		addEntry(subProcess);
	}

	@Override
	public void updateSubProcess(BigBangProcess subProcess) {
		for(ValueSelectable<BigBangProcess> entry : this){
			if(entry.getValue().dataId.equalsIgnoreCase(subProcess.dataId)){
				entry.setValue(subProcess);
				break;
			}
		}
	}

	@Override
	public void deleteSubProcess(String id) {
		for(ValueSelectable<BigBangProcess> entry : this){
			if(entry.getValue().dataId.equalsIgnoreCase(id)){
				remove(entry);
				break;
			}
		}
	}

}