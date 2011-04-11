package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.client.userInterface.ListEntry;
import bigBang.module.generalSystemModule.shared.InsuranceAgency;

public class InsuranceAgencyListEntry extends ListEntry<InsuranceAgency> {
	
	public InsuranceAgencyListEntry(InsuranceAgency value) {
		super(value);
		setHeight("40px");
		setInfo(value);
	}
	
	@Override
	public <I extends Object> void setInfo(I infoGeneric){
		InsuranceAgency info = (InsuranceAgency) infoGeneric;
		
		if(info.id == null) {
			setTitle("Nova Seguradora");
			return;
		}

		setTitle(info.name);
		setText(info.acronym);
	}

}
