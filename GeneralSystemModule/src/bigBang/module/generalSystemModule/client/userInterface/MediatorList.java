package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.module.generalSystemModule.shared.Mediator;

public class MediatorList extends FilterableList<Mediator> {

	public HasClickHandlers refreshButton;
	public HasClickHandlers newButton;
	
	public MediatorList() {
		super();
		ListHeader header = new ListHeader();
		header.setText("Mediadores");
		header.showNewButton("Novo");
		header.showRefreshButton();
		refreshButton = header.getRefreshButton();
		newButton = header.getNewButton();
		setHeaderWidget(header);
		onSizeChanged();
	}
	
	@Override
	public void updateFooterText() {
		int size = size();
		String text = new String();
		if(size == 0)
			text = "Sem mediadores";
		if(size == 1)
			text = "1 mediador";
		if(size > 1)
			text = size + " mediadores";
		
		setFooterText(text);
	}

}
