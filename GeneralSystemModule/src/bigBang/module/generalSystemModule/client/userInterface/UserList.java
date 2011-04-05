package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.module.generalSystemModule.shared.User;

public class UserList extends FilterableList<User> {
	
	protected String filterText;
	
	public HasClickHandlers refreshButton;
	public HasClickHandlers newButton;
	
	public UserList(){
		super();
		ListHeader header = new ListHeader();
		header.setText("Utilizadores");
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
			text = "Sem Utilizadores";
			break;
		case 1:
			text = "1 Utilizador";
			break;
		default:
			text = size + " Utilizadores";
			break;
		}
		
		setFooterText(text);
	}

}
