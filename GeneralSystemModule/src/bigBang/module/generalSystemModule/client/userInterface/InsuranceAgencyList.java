package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.module.generalSystemModule.shared.InsuranceAgency;

public class InsuranceAgencyList extends FilterableList<InsuranceAgency> {

	public HasClickHandlers refreshButton;
	public HasClickHandlers newButton;
	
	public InsuranceAgencyList(){
		super();
		ListHeader header = new ListHeader();
		header.setText("Seguradoras");
		header.showNewButton("Novo");
		header.showRefreshButton();
		refreshButton = header.getRefreshButton();
		newButton = header.getNewButton();
		setHeaderWidget(header);
		onSizeChanged();
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
	}

}
