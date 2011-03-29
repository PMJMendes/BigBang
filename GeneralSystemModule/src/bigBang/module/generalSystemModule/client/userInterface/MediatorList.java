package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.module.generalSystemModule.shared.Mediator;

public class MediatorList extends List<Mediator> {

	public MediatorList() {
		super();
		ListHeader header = new ListHeader();
		header.setText("Mediadores");
		setHeaderWidget(header);
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
