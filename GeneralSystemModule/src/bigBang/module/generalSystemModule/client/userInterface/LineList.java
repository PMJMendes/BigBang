package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.module.generalSystemModule.shared.Line;

public class LineList extends FilterableList<Line> {

	private HasClickHandlers newButton;
	
	public LineList(){
		ListHeader header = new ListHeader();
		header.setText("Ramos");
		header.showNewButton("Novo");
		this.newButton = header.getNewButton();
		
		setHeaderWidget(header);
	}
	
	public HasClickHandlers getNewButton(){
		return this.newButton;
	}
	
}
