package bigBang.library.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class BigBangMenuItem extends MenuItem {

	protected String defaultColor;
	protected final String DISABLED_COLOR = "#BBB";
	
	public BigBangMenuItem(String string, MenuBar submenu) {
		super(string, submenu);
		defaultColor = this.getElement().getStyle().getColor();
	}
	

	public BigBangMenuItem(String string, Command command) {
		super(string, command);
		defaultColor = this.getElement().getStyle().getColor();
	}


	@Override
	public void setEnabled(boolean enabled) {
		GWT.log(this.getText());
		super.setEnabled(enabled);
		this.getElement().getStyle().setColor(enabled ? defaultColor : DISABLED_COLOR);
	}
}
