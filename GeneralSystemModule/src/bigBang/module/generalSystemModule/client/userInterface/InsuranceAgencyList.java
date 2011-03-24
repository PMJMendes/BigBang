package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.client.userInterface.List;
import bigBang.module.generalSystemModule.shared.InsuranceAgency;

public class InsuranceAgencyList extends List<InsuranceAgency> {

	public InsuranceAgencyList(){
		super();
		setHeaderText("Seguradoras");
		updateFooterLabel();
	}
	
	private void updateFooterLabel(){
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
	}
}
