package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;

import bigBang.definitions.client.dataAccess.InsuranceAgencyBroker;
import bigBang.definitions.client.dataAccess.InsuranceAgencyDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;

public class InsuranceAgencyList extends FilterableList<InsuranceAgency> implements InsuranceAgencyDataBrokerClient {

	protected ListHeader header;
	protected int insuranceAgencyDataVersion;
	
	public InsuranceAgencyList(){
		super();
		header = new ListHeader();
		header.setText("Seguradoras");
		header.showNewButton("Novo");
		header.showRefreshButton();
		setHeaderWidget(header);
		onSizeChanged();
		showFilterField(false);
	}
	
	@Override
	protected void onSizeChanged(){
		int size = this.size();
		String text;
		switch(size){
		case 0:
			text = "Sem Seguradoras";
			break;
		case 1:
			text = "1 Seguradora";
			break;
		default:
			text = size + " Seguradoras";
			break;
		}
		
		setFooterText(text);
		
		((InsuranceAgencyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_AGENCY)).registerClient(this);
	}
	
	public HasClickHandlers getNewButton(){
		return header.getNewButton();
	}
	
	public HasClickHandlers getRefreshButton(){
		return header.getRefreshButton();
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equals(BigBangConstants.EntityIds.INSURANCE_AGENCY)){
			this.insuranceAgencyDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equals(BigBangConstants.EntityIds.INSURANCE_AGENCY)){
			return this.insuranceAgencyDataVersion;
		}
		return -1;
	}

	@Override
	public void setInsuranceAgencies(InsuranceAgency[] insuranceAgencies) {
		clear();
		for(int i = 0; i < insuranceAgencies.length; i++) {
			add(new InsuranceAgencyListEntry(insuranceAgencies[i]));
		}
	}

	@Override
	public void addInsuranceAgency(InsuranceAgency insuranceAgency) {
		add(0, new InsuranceAgencyListEntry(insuranceAgency));
	}

	@Override
	public void updateInsuranceAgency(InsuranceAgency insuranceAgency) {
		for(ValueSelectable<InsuranceAgency> vs : this) {
			if(insuranceAgency.id.equals(vs.getValue().id)){
				vs.setValue(insuranceAgency, false);
				break;
			}
		}
	}

	@Override
	public void removeInsuranceAgency(String insuranceAgencyId) {
		for(ValueSelectable<InsuranceAgency> vs : this) {
			if(insuranceAgencyId.equals(vs.getValue().id)){
				remove(vs);
				break;
			}
		}
	}

}
