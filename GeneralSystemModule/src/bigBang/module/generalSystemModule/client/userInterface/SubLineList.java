package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.module.generalSystemModule.shared.SubLine;

public class SubLineList extends FilterableList<SubLine> {
	private HasClickHandlers newButton;

	public SubLineList(){
		ListHeader header = new ListHeader();
		header.setText("Modalidades");
		header.showNewButton("Nova");
		this.newButton = header.getNewButton();

		setHeaderWidget(header);
	}

	public HasClickHandlers getNewButton(){
		return this.newButton;
	}
}
