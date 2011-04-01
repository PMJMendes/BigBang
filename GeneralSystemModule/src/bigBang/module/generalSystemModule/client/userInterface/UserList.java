package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.module.generalSystemModule.shared.User;

public class UserList extends FilterableList<User> {
	
	protected String filterText;
	
	public UserList(){
		super();
		updateFooterLabel();
	}

	private void updateFooterLabel(){
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
