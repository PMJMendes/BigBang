package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.shared.userInterface.List;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserRole;

public class UserList extends List<String> {
	
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
