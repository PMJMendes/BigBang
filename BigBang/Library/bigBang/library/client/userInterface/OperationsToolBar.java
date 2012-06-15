package bigBang.library.client.userInterface;

import java.util.List;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class OperationsToolBar extends MenuBar {

	public OperationsToolBar(){
		this(false);
	}
	
	public OperationsToolBar(boolean vertical){
		super(vertical);
		setWidth("100%");
		this.getElement().getStyle().setBackgroundImage("images/listHeaderBackground.png");
		this.setFocusOnHoverEnabled(false);
	}
	
	public List<MenuItem> getMenuItems(){
		return getItems();
	}

}
