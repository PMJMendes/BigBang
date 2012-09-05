package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.client.dataAccess.InsuredObjectDataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.InsuredObjectOLD;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;

public class InsuredObjectSearchPanel extends SearchPanel<InsuredObjectStub> implements InsuredObjectDataBrokerClient{

	public static class Entry extends ListEntry<InsuredObjectStub>{

		protected Label name;

		public Entry(InsuredObjectStub object){
			super(object);
			setHeight("30px");
		}

		public <I extends Object> void setInfo(I info){
			InsuredObjectStub value = (InsuredObjectStub) info;

			if(value.id != null){
				if(name == null){
					name = getFormatedLabel();
					setWidget(name);
				}
				
				name.setText(value.unitIdentification);
			}
			
			
		}
	}


	public InsuredObjectSearchPanel(SearchDataBroker<InsuredObjectStub> broker) { //TODO ESTE BROKER NAO ESTÁ CORRECTO
		super(broker);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDataVersion(String dataElementId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeInsuredObject(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remapItemId(String newId, String oldId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSearch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(Collection<InsuredObjectStub> results) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addInsuredObject(InsuredObjectOLD object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInsuredObject(InsuredObjectOLD object) {
		// TODO Auto-generated method stub
		
	}



}
