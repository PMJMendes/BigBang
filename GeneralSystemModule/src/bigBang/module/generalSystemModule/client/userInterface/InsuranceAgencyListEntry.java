package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.client.userInterface.ListEntry;
import bigBang.module.generalSystemModule.shared.InsuranceAgency;

public class InsuranceAgencyListEntry extends ListEntry<InsuranceAgency> {
	
	public InsuranceAgencyListEntry(InsuranceAgency value) {
		super(value);
		setInfo(value);
	}
	
	public <I extends Object> void setInfo(I info) {
		InsuranceAgency value = (InsuranceAgency) info;
		setTitle(value.name);
		setText(value.acronym);
	};

}
