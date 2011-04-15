package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.module.generalSystemModule.shared.Coverage;

public class CoverageList extends FilterableList<Coverage> {

	private HasClickHandlers newButton;

	public CoverageList(){
		ListHeader header = new ListHeader();
		header.setText("Coberturas");
		header.showNewButton("Nova");
		this.newButton = header.getNewButton();

		setHeaderWidget(header);
	}

	public HasClickHandlers getNewButton(){
		return this.newButton;
	}
}
