package bigBang.library.client;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

public interface Operation extends Identifiable {
	
	public void init();
	
	public String getId();
	
	public AbstractImagePrototype getIcon();
	
	public String getDescription();
	
	public String getShortDescription();
	
	public String getOwnerProcessId();
	
	public boolean getPermission();
	
	public void setPermission(boolean p);
	
}
